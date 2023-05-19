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

package io.element.android.features.roomdetails.impl.edition

import android.net.Uri
import io.element.android.features.createroom.api.ui.AvatarAction
import io.element.android.libraries.architecture.Async
import kotlinx.collections.immutable.ImmutableList

data class RoomDetailsEditionState(
    val roomId: String,
    val roomName: String,
    val roomTopic: String,
    val roomAvatarUrl: Uri?,
    val avatarActions: ImmutableList<AvatarAction>,
    val saveButtonVisible: Boolean,
    val saveAction: Async<Unit>,
    val eventSink: (RoomDetailsEditionEvents) -> Unit
)
