/*
 * Copyright 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package io.element.android.features.maprealtime.impl

// TODO add your ui models. Remove the eventSink if you don't have events.
// TODO (tb): This all could live on the MessagesState as we are only using the View aspect and not the Presenter aspect.
// Refactor this to be a part of the MessagesState in the future.
// Do not use default value, so no member get forgotten in the presenters.
data class MapRealtimePresenterState(
    val permissionDialog: Dialog,
    val hasLocationPermission: Boolean,
    val showMapTypeDialog: Boolean,
    val appName: String,
    val roomName: String,
    val eventSink: (MapRealtimeEvents) -> Unit,
    val isSharingLocation: Boolean,
    val mapType: MapType,
    val vehicleLocations: List<MapRealtimeLocationDot>
) {
    sealed interface Dialog {
        data object None : Dialog
        data object PermissionRationale : Dialog
        data object PermissionDenied : Dialog
    }

    val styleUrl: String
        get() = "https://api.maptiler.com/maps/" + mapType.mapKey + "/style.json?key=" + "4N19bSbSelzpOSfUibeB"
}
