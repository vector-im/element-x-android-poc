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

package io.element.android.libraries.designsystem.components.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.element.android.libraries.designsystem.components.dialogs.MultipleSelectionDialog
import io.element.android.libraries.designsystem.preview.ElementThemedPreview
import io.element.android.libraries.designsystem.theme.components.ListItem
import io.element.android.libraries.designsystem.theme.components.Text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MultipleSelectionListItem(
    headline: String,
    options: ImmutableList<String>,
    onSelectionChanged: (List<Int>) -> Unit,
    resultFormatter: (List<Int>) -> String?,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    leadingContent: ListItemContent? = null,
    selected: List<Int> = emptyList(),
    displayResultInTrailingContent: Boolean = false,
) {
    val selectedIndexes = remember(selected) { selected.toMutableStateList() }
    val selectedItemsText by remember { derivedStateOf { resultFormatter(selectedIndexes) } }
    val decoratedSupportedText: @Composable (() -> Unit)? = if (!selectedItemsText.isNullOrBlank() && !displayResultInTrailingContent) {
        @Composable {
            Text(selectedItemsText!!)
        }
    } else if (supportingText != null) {
        @Composable {
            Text(supportingText)
        }
    } else {
        null
    }
    val trailingContent: ListItemContent? = if (!selectedItemsText.isNullOrBlank() && displayResultInTrailingContent) {
        ListItemContent.Text(selectedItemsText!!)
    } else {
        null
    }

    var displaySelectionDialog by rememberSaveable { mutableStateOf(false) }

    ListItem(
        modifier = modifier,
        headlineContent = { Text(text = headline) },
        supportingContent = decoratedSupportedText,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        onClick = { displaySelectionDialog = true }
    )

    if (displaySelectionDialog) {
        MultipleSelectionDialog(
            title = headline,
            options = options,
            onConfirmClicked = { newSelectedIndexes ->
                if (newSelectedIndexes != selectedIndexes.toList()) {
                    onSelectionChanged(newSelectedIndexes)
                    selectedIndexes.clear()
                    selectedIndexes.addAll(newSelectedIndexes)
                }
                displaySelectionDialog = false
            },
            onDismissRequest = { displaySelectionDialog = false },
            initialSelection = selectedIndexes,
        )
    }
}

@Preview("Multiple selection List item - no selection")
@Composable
internal fun MutipleSelectionListItemPreview() {
    ElementThemedPreview {
        val options = persistentListOf("Option 1", "Option 2", "Option 3")
        val selected = persistentListOf<Int>()
        MultipleSelectionListItem(
            headline = "Headline",
            options = options,
            onSelectionChanged = {},
            supportingText = "Supporting text",
            resultFormatter = { options.mapIndexedNotNull { index, value -> value.takeIf { selected.contains(index) } }.joinToString(", ") },
        )
    }
}

@Preview("Multiple selection List item - selection in supporting text")
@Composable
internal fun MutipleSelectionListItemSelectedPreview() {
    ElementThemedPreview {
        val options = persistentListOf("Option 1", "Option 2", "Option 3")
        val selected = persistentListOf<Int>(0, 2)
        MultipleSelectionListItem(
            headline = "Headline",
            options = options,
            onSelectionChanged = {},
            supportingText = "Supporting text",
            resultFormatter = {
                val selectedValues = options.mapIndexedNotNull { index, value -> value.takeIf { selected.contains(index) } }.joinToString(", ")
                "Selected: $selectedValues"
            },
        )
    }
}

@Preview("Multiple selection List item - selection in trailing content")
@Composable
internal fun MutipleSelectionListItemSelectedTrailingContentPreview() {
    ElementThemedPreview {
        val options = persistentListOf("Option 1", "Option 2", "Option 3")
        val selected = persistentListOf<Int>(0, 2)
        MultipleSelectionListItem(
            headline = "Headline",
            options = options,
            onSelectionChanged = {},
            supportingText = "Supporting text",
            resultFormatter = { selected.size.toString() },
            displayResultInTrailingContent = true,
        )
    }
}
