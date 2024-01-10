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

package io.element.android.features.call.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class WidgetMessage(
    @SerialName("api") val direction: Direction,
    @SerialName("widgetId") val widgetId: String,
    @SerialName("requestId") val requestId: String,
    @SerialName("action") val action: Action,
    @SerialName("data") val data: JsonElement? = null,
) {

    @Serializable
    enum class Direction {
        @SerialName("fromWidget")
        FromWidget,

        @SerialName("toWidget")
        ToWidget
    }

    @Serializable
    enum class Action {
        @SerialName("im.vector.hangup")
        HangUp,

        @SerialName("send_event")
        SendEvent,
    }
}
