/*
 * Copyright (c) 2022 New Vector Ltd
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

package io.element.android.libraries.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.element.android.libraries.designsystem.preview.ElementPreviews
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.element.android.libraries.designsystem.components.dialogs.DialogPreview
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewGroup
import io.element.android.libraries.designsystem.theme.components.CircularProgressIndicator
import io.element.android.libraries.designsystem.theme.components.Text

@Composable
fun ProgressDialog(
    modifier: Modifier = Modifier,
    text: String? = null,
    type: ProgressDialogType = ProgressDialogType.Indeterminate,
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        ProgressDialogContent(
            modifier = modifier,
            text = text,
            progressIndicator = {
                when (type) {
                    is ProgressDialogType.Indeterminate -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is ProgressDialogType.Determinate -> {
                        CircularProgressIndicator(
                            progress = type.progress,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        )
    }
}

@Immutable
sealed interface ProgressDialogType {
    data class Determinate(val progress: Float) : ProgressDialogType
    object Indeterminate : ProgressDialogType
}

@Composable
private fun ProgressDialogContent(
    modifier: Modifier = Modifier,
    text: String? = null,
    progressIndicator: @Composable () -> Unit = {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 38.dp, bottom = 32.dp, start = 40.dp, end = 40.dp)
        ) {
            progressIndicator()
            if (!text.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@ElementPreviews(group = PreviewGroup.Dialogs)
@Composable
internal fun ProgressDialogPreview() {
    ElementPreview { ContentToPreview() }
}

@Composable
private fun ContentToPreview() {
    DialogPreview {
        ProgressDialogContent(text = "test dialog content")
    }
}
