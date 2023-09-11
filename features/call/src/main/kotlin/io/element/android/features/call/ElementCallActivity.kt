/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.element.android.features.call

import android.Manifest
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.PermissionRequest
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import io.element.android.libraries.designsystem.components.button.BackButton
import io.element.android.libraries.designsystem.theme.components.Scaffold
import io.element.android.libraries.designsystem.theme.components.Text
import io.element.android.libraries.designsystem.theme.components.TopAppBar
import io.element.android.libraries.theme.ElementTheme
import java.net.URLDecoder

class ElementCallActivity : ComponentActivity() {

    private lateinit var audioManager: AudioManager

    private var webkitPermissionRequest: PermissionRequest? = null

    private var audiofocusRequest: AudioFocusRequest? = null
    private var audioFocusChangeListener: AudioManager.OnAudioFocusChangeListener? = null

    private var urlState = mutableStateOf<String?>(null)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val fallbackUrl = "https://call.element.io"

        urlState.value = intent?.dataString?.let(::parseUrl) ?: run {
            finish()
            return
        }

        val requestPermissionsLauncher = registerPermissionResultLauncher()

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        requestAudioFocus()

        setContent {
            ElementTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Element Call") },
                            navigationIcon = {
                                BackButton(
                                    imageVector = Icons.Default.Close,
                                    onClick = { onBackPressed() }
                                )
                            }
                        )
                    }
                ) { padding ->
                    CallWebView(
                        modifier = Modifier
                                .padding(padding)
                                .consumeWindowInsets(padding)
                                .fillMaxSize(),
                        url = urlState.value ?: fallbackUrl,
                        onPermissionsRequested = {
                            webkitPermissionRequest = it
                            // TODO: match permissions, don't blindly ask camera and audio
                            requestPermissionsLauncher.launch(
                                arrayOf(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val intentUrl = intent?.dataString?.let(::parseUrl) ?: return
        urlState.value = intentUrl
    }

    override fun onResume() {
        super.onResume()
        CallForegroundService.stop(this)
    }

    override fun onPause() {
        super.onPause()
        if (!isFinishing) {
            CallForegroundService.start(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseAudioFocus()
        CallForegroundService.stop(this)
    }

    private fun parseUrl(url: String?): String? {
        if (url == null) return null
        val parsedUrl = Uri.parse(url)
        val scheme = parsedUrl.scheme ?: return null
        return when {
            scheme in sequenceOf("http", "https") -> url
            scheme == "element" && parsedUrl.host == "call" -> {
                parsedUrl.getQueryParameter("url")?.let { URLDecoder.decode(it, "utf-8") }
            }
            // This should never be possible, but we still need to take into account the possibility
            else -> null
        }
    }

    private fun registerPermissionResultLauncher(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val request = webkitPermissionRequest ?: return@registerForActivityResult
            val permissionsToGrant = mutableListOf<String>()
            permissions.forEach { (permission, granted) ->
                if (granted) {
                    val webKitPermission = when (permission) {
                        Manifest.permission.CAMERA -> PermissionRequest.RESOURCE_VIDEO_CAPTURE
                        Manifest.permission.RECORD_AUDIO -> PermissionRequest.RESOURCE_AUDIO_CAPTURE
                        else -> return@forEach
                    }
                    permissionsToGrant.add(webKitPermission)
                }
            }
            request.grant(permissionsToGrant.toTypedArray())
        }
    }

    private fun requestAudioFocus() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .build()
            audioManager.requestAudioFocus(request)
            audiofocusRequest = request
        } else {
            val listener = object : AudioManager.OnAudioFocusChangeListener {
                override fun onAudioFocusChange(focusChange: Int) {

                }
            }
            audioManager.requestAudioFocus(
                listener,
                AudioManager.STREAM_VOICE_CALL,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE,
            )

            audioFocusChangeListener = listener
        }
    }

    private fun releaseAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audiofocusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
        } else {
            audioFocusChangeListener?.let { audioManager.abandonAudioFocus(it) }
        }
    }

}
