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

package io.element.android.libraries.matrix.impl.sync

import io.element.android.libraries.matrix.api.sync.SyncService
import io.element.android.libraries.matrix.api.sync.SyncState
import io.element.android.libraries.matrix.impl.room.stateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.matrix.rustcomponents.sdk.RoomListService
import org.matrix.rustcomponents.sdk.RoomListServiceState

class RustSyncService(
    private val roomListService: RoomListService,
    private val sessionCoroutineScope: CoroutineScope
) : SyncService {

    override fun startSync() {
        if (!roomListService.isSyncing()) {
            roomListService.sync()
        }
    }

    override fun stopSync() {
        if (roomListService.isSyncing()) {
            roomListService.stopSync()
        }
    }

    override val syncState: StateFlow<SyncState> =
        roomListService
            .stateFlow()
            .map(RoomListServiceState::toSyncState)
            .distinctUntilChanged()
            .stateIn(sessionCoroutineScope, SharingStarted.WhileSubscribed(), SyncState.Idle)
}
