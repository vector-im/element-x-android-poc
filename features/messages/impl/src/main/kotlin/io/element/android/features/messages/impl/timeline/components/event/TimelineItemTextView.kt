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

package io.element.android.features.messages.impl.timeline.components.event

import android.text.SpannableString
import android.text.util.Linkify
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.core.text.buildSpannedString
import androidx.core.text.util.LinkifyCompat
import io.element.android.compound.theme.ElementTheme
import io.element.android.compound.theme.LinkColor
import io.element.android.features.messages.impl.timeline.model.event.TimelineItemTextBasedContent
import io.element.android.features.messages.impl.timeline.model.event.TimelineItemTextBasedContentProvider
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight
import io.element.android.wysiwyg.compose.EditorStyledText
import io.element.android.wysiwyg.compose.LinkStyle
import io.element.android.wysiwyg.compose.RichTextEditorDefaults

@Composable
fun TimelineItemTextView(
    content: TimelineItemTextBasedContent,
    extraPadding: ExtraPadding,
    onLinkClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalContentColor provides ElementTheme.colors.textPrimary) {
        val fontSize = LocalTextStyle.current.fontSize

        val formattedBody = content.formattedBody
        val body = SpannableString(formattedBody ?: content.body)
        LinkifyCompat.addLinks(body, Linkify.WEB_URLS or Linkify.PHONE_NUMBERS)

        Box(modifier) {
            val textWithPadding = remember(body, fontSize) {
                buildSpannedString {
                    append(body)
                    append(extraPadding.getStr(fontSize))
                }
            }
            EditorStyledText(
                text = textWithPadding,
                onLinkClickedListener = onLinkClicked,
                style = RichTextEditorDefaults.style(
                    text = RichTextEditorDefaults.textStyle(
                        // TODO re-enable this once it's available in the RTE side
                        // includeFontPadding = false
                    ),
                    link = LinkStyle(LinkColor)
                )
            )
        }
    }
}

@PreviewsDayNight
@Composable
internal fun TimelineItemTextViewPreview(
    @PreviewParameter(TimelineItemTextBasedContentProvider::class) content: TimelineItemTextBasedContent
) = ElementPreview {
    TimelineItemTextView(
        content = content,
        extraPadding = ExtraPadding(nbChars = 8),
        onLinkClicked = {},
    )
}
