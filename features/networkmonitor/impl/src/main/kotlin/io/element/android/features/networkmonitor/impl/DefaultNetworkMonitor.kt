/*
 * Copyright 2023, 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.networkmonitor.impl

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.features.networkmonitor.api.NetworkMonitor
import io.element.android.features.networkmonitor.api.NetworkStatus
import io.element.android.libraries.di.SessionScope
import io.element.android.libraries.di.SingleIn
import io.element.android.libraries.matrix.api.MatrixClient
import io.element.android.libraries.matrix.api.sync.SyncState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@ContributesBinding(scope = SessionScope::class)
@SingleIn(SessionScope::class)
class DefaultNetworkMonitor @Inject constructor(
    matrixClient: MatrixClient,
    appCoroutineScope: CoroutineScope,
) : NetworkMonitor {
    override val connectivity: StateFlow<NetworkStatus> = matrixClient
        .syncService()
        .syncState
        .map { syncState ->
            syncState.toNetworkStatus()
        }
        .onEach {
            Timber.d("NetworkStatus changed=$it")
        }
        .stateIn(
            scope = appCoroutineScope, started = SharingStarted.WhileSubscribed(),
            initialValue = matrixClient
                .syncService()
                .syncState
                .value
                .toNetworkStatus()
        )

    private fun SyncState.toNetworkStatus(): NetworkStatus = when (this) {
        SyncState.Idle,
        SyncState.Running,
        SyncState.Error,
        SyncState.Terminated -> NetworkStatus.Online
        SyncState.Offline -> NetworkStatus.Offline
    }
}
