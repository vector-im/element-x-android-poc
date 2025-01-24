/*
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.appnav.di

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.di.SingleIn
import io.element.android.libraries.matrix.api.core.SessionId
import io.element.android.libraries.matrix.api.sync.SyncOrchestrator
import io.element.android.libraries.matrix.api.sync.SyncOrchestratorProvider
import javax.inject.Inject

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DefaultSyncOrchestratorProvider @Inject constructor(
    private val matrixClientsHolder: MatrixClientsHolder,
) : SyncOrchestratorProvider {
    override fun get(sessionId: SessionId): SyncOrchestrator? {
        return matrixClientsHolder.getSyncOrchestratorOrNull(sessionId)
    }
}
