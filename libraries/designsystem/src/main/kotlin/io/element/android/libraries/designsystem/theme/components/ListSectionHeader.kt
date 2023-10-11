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

package io.element.android.libraries.designsystem.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.element.android.libraries.designsystem.preview.ElementThemedPreview
import io.element.android.libraries.designsystem.preview.PreviewGroup
import io.element.android.libraries.theme.ElementTheme

// Designs: https://www.figma.com/file/G1xy0HDZKJf5TCRFmKb5d5/Compound-Android-Components?type=design&node-id=425%3A24208&mode=design&t=G5hCfkLB6GgXDuWe-1

/**
 * List section header.
 * @param title The title of the section.
 * @param modifier The modifier to be applied to the section.
 * @param hasDivider Whether to show a divider above the section or not. Default is `true`.
 * @param description A description for the section. It's empty by default.
 */
@Composable
fun ListSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    hasDivider: Boolean = true,
    description: @Composable () -> Unit = {},
) {
    Column(modifier.fillMaxWidth()) {
        if (hasDivider) {
            HorizontalDivider(modifier = Modifier.padding(top = 16.dp))
        }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = ElementTheme.typography.fontBodyLgMedium,
                color = ElementTheme.colors.textPrimary,
            )
            CompositionLocalProvider(
                LocalTextStyle provides ElementTheme.typography.fontBodySmRegular,
                LocalContentColor provides ElementTheme.colors.textSecondary,
            ) {
                description()
            }
        }
    }
}

@Preview(group = PreviewGroup.ListSections, name = "List section header")
@Composable
internal fun ListSectionHeaderPreview() {
    ElementThemedPreview {
        ListSectionHeader(
            title = "List section",
            hasDivider = false,
        )
    }
}

@Preview(group = PreviewGroup.ListSections, name = "List section header with divider")
@Composable
internal fun ListSectionHeaderWithDividerPreview() {
    ElementThemedPreview {
        ListSectionHeader(
            title = "List section",
            hasDivider = true,
        )
    }
}

@Preview(group = PreviewGroup.ListSections, name = "List section header with description")
@Composable
internal fun ListSectionHeaderWithDescriptionPreview() {
    ElementThemedPreview {
        ListSectionHeader(
            title = "List section",
            description = {
                ListSupportingText(
                    text = "Supporting line text lorem ipsum dolor sit amet, consectetur. Read more",
                    contentPadding = ListSupportingTextDefaults.Padding.None,
                )
            },
            hasDivider = false,
        )
    }
}

@Preview(group = PreviewGroup.ListSections, name = "List section header with description and divider")
@Composable
internal fun ListSectionHeaderWithDescriptionAndDividerPreview() {
    ElementThemedPreview {
        ListSectionHeader(
            title = "List section",
            description = {
                ListSupportingText(
                    text = "Supporting line text lorem ipsum dolor sit amet, consectetur. Read more",
                    contentPadding = ListSupportingTextDefaults.Padding.None,
                )
            },
            hasDivider = true,
        )
    }
}
