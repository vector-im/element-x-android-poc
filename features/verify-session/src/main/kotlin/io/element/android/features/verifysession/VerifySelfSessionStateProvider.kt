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

package io.element.android.features.verifysession

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.element.android.libraries.architecture.Async

open class VerifySelfSessionStateProvider : PreviewParameterProvider<VerifySelfSessionState> {
    override val values: Sequence<VerifySelfSessionState>
        get() = sequenceOf(
            aTemplateState(),
            aTemplateState().copy(state = VerificationState.AwaitingOtherDeviceResponse),
            aTemplateState().copy(state = VerificationState.Verifying(aEmojiEntryList(),  Async.Uninitialized)),
            aTemplateState().copy(state = VerificationState.Verifying(aEmojiEntryList(),  Async.Loading())),
            aTemplateState().copy(state = VerificationState.Canceled),
            // Add other state here
        )
}

fun aTemplateState() = VerifySelfSessionState(
    state = VerificationState.Initial,
    eventSink = {},
)

fun aEmojiEntryList() = listOf(
    EmojiEntry("🍕", "Pizza"),
    EmojiEntry("🚀", "Rocket"),
    EmojiEntry("🚀", "Rocket"),
    EmojiEntry("🗺️", "Map"),
    EmojiEntry("🎳", "Bowling"),
    EmojiEntry("🎳", "Bowling"),
    EmojiEntry("📌", "Pin"),
)
