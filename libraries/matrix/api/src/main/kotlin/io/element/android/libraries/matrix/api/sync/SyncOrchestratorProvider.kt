/*
 * Copyright 2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.libraries.matrix.api.sync

import io.element.android.libraries.matrix.api.core.SessionId

/**
 * Provides a [SyncOrchestrator] for a given [SessionId].
 */
fun interface SyncOrchestratorProvider {
    /**
     * Get a [SyncOrchestrator] for the given [SessionId].
     */
    fun get(sessionId: SessionId): SyncOrchestrator?
}
