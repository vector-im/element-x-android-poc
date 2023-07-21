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

package io.element.android.features.messages.impl.timeline.components.reactionsummary

import io.element.android.features.messages.impl.timeline.model.AggregatedReaction
import io.element.android.libraries.matrix.api.core.EventId
import io.element.android.libraries.matrix.api.room.RoomMember
import kotlinx.collections.immutable.ImmutableList

data class ReactionSummaryState(
    val target: Target,
    val members: ImmutableList<RoomMember>,
    val eventSink: (ReactionSummaryEvents) -> Unit
){
    sealed interface Target {
        object None : Target
        data class Summary(
            val reactions: List<AggregatedReaction>,
            val selectedKey: String,
            val selectedEventId: EventId
        ) : Target
    }
}

