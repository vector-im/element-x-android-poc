/*
 * Copyright 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.roomlist.impl.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.element.android.features.roomlist.impl.R
import io.element.android.libraries.designsystem.components.Announcement
import io.element.android.libraries.designsystem.components.AnnouncementType
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight

@Composable
internal fun NativeSlidingSyncMigrationBanner(
    onContinueClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Announcement(
        modifier = modifier.roomListBannerPadding(),
        title = stringResource(R.string.banner_migrate_to_native_sliding_sync_title),
        description = stringResource(R.string.banner_migrate_to_native_sliding_sync_description),
        type = AnnouncementType.Actionable(
            actionText = stringResource(R.string.banner_migrate_to_native_sliding_sync_action),
            onActionClick = onContinueClick,
            onDismissClick = onDismissClick,
        )
    )
}

@PreviewsDayNight
@Composable
internal fun NativeSlidingSyncMigrationBannerPreview() = ElementPreview {
    NativeSlidingSyncMigrationBanner(
        onContinueClick = {},
        onDismissClick = {},
    )
}
