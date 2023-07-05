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

package io.element.android.libraries.designsystem.components.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.element.android.libraries.designsystem.preview.ElementPreviews
import androidx.compose.ui.unit.dp
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewGroup
import io.element.android.libraries.designsystem.theme.components.Text
import io.element.android.libraries.theme.ElementTheme

@Composable
fun PreferenceCategory(
    modifier: Modifier = Modifier,
    title: String? = null,
    showDivider: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (title != null) {
            PreferenceCategoryTitle(title = title)
        }
        content()
        if (showDivider) {
            PreferenceDivider()
        }
    }
}

@Composable
fun PreferenceCategoryTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(
            top = 20.dp,
            bottom = 8.dp,
            start = preferencePaddingHorizontal,
            end = preferencePaddingHorizontal,
        ),
        style = ElementTheme.typography.fontBodyLgMedium,
        color = ElementTheme.materialColors.primary,
        text = title,
    )
}

@ElementPreviews(group = PreviewGroup.Preferences)
@Composable
internal fun PreferenceCategoryPreview() {
    ElementPreview { ContentToPreview() }
}

@Composable
private fun ContentToPreview() {
    PreferenceCategory(
        title = "Category title",
    ) {
        PreferenceText(
            title = "Title",
            icon = Icons.Default.BugReport,
        )
        PreferenceSwitch(
            title = "Switch",
            icon = Icons.Default.Announcement,
            isChecked = true
        )
        PreferenceSlide(
            title = "Slide",
            summary = "Summary",
            value = 0.75F,
            showIconAreaIfNoIcon = true,
        )
    }
}
