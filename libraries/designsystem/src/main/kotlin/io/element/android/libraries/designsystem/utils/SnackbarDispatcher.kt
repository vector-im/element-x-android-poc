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

package io.element.android.libraries.designsystem.utils

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.coroutineContext

class SnackbarDispatcher {
    private val mutex = Mutex()

    private val snackbarState = MutableStateFlow<SnackbarMessage?>(null)
    val snackbarMessage: Flow<SnackbarMessage?> = snackbarState
//        .onEach { value ->
//            if (value != null) {
//                CoroutineScope(coroutineContext + Dispatchers.Main).launch {
//                    delay(10)
//                    clear()
//                }
//            }
//        }

    suspend fun post(message: SnackbarMessage) {
        mutex.withLock {
            snackbarState.update { message }
        }
    }

    suspend fun clear() {
        mutex.withLock {
            snackbarState.update { null }
        }
    }
}

data class SnackbarMessage(
    @StringRes val messageResId: Int,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    @StringRes val actionResId: Int? = null,
    val action: () -> Unit = {},
)
