/*
 * Copyright 2023, 2024 New Vector Ltd.
 * Copyright 2021 Google LLC
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
@file:Suppress("MatchingDeclarationName")

package io.element.android.libraries.maplibre.compose

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.LocationComponentOptions
import org.maplibre.android.location.OnCameraTrackingChangedListener
import org.maplibre.android.location.engine.LocationEngineRequest
import org.maplibre.android.location.modes.RenderMode
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style

private const val LOCATION_REQUEST_INTERVAL = 750L

@SuppressLint("MissingPermission")
internal class MapPropertiesNode(
    val map: MapLibreMap,
    style: Style,
    context: Context,
    cameraPositionState: CameraPositionState,
    locationSettings: MapLocationSettings,
    private val onMapLongClick: ((LatLng) -> Unit)? = null,
) : MapNode {

    init {
        val locationComponentOptionBuilder = LocationComponentOptions.builder(context)
            .pulseEnabled(locationSettings.pulseEnabled)

        if (locationSettings.backgroundTintColor != Color.Unspecified) {
            locationComponentOptionBuilder.backgroundTintColor(locationSettings.backgroundTintColor.toArgb())
        }

        if (locationSettings.foregroundTintColor != Color.Unspecified) {
            locationComponentOptionBuilder.foregroundTintColor(locationSettings.foregroundTintColor.toArgb())
        }

        if (locationSettings.backgroundStaleTintColor != Color.Unspecified) {
            locationComponentOptionBuilder.backgroundStaleTintColor(locationSettings.backgroundStaleTintColor.toArgb())
        }

        if (locationSettings.foregroundStaleTintColor != Color.Unspecified) {
            locationComponentOptionBuilder.foregroundStaleTintColor(locationSettings.foregroundStaleTintColor.toArgb())
        }

        if (locationSettings.accuracyColor != Color.Unspecified) {
            locationComponentOptionBuilder.accuracyColor(locationSettings.accuracyColor.toArgb())
        }

        if (locationSettings.pulseColor != Color.Unspecified) {
            locationComponentOptionBuilder.pulseColor(locationSettings.pulseColor.toArgb())
        }

        map.locationComponent.activateLocationComponent(
            LocationComponentActivationOptions.Builder(context, style)
                .locationComponentOptions(locationComponentOptionBuilder.build())
                .locationEngineRequest(
                    LocationEngineRequest.Builder(LOCATION_REQUEST_INTERVAL)
                        .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                        .setFastestInterval(LOCATION_REQUEST_INTERVAL)
                        .build()
                )
                .build()
        )

        cameraPositionState.setMap(map)
    }

    internal var cameraPositionState = cameraPositionState
        set(value) {
            if (value == field) return
            field.setMap(null)
            field = value
            value.setMap(map)
        }

    override fun onAttached() {
        map.addOnCameraIdleListener {
            cameraPositionState.isMoving = false
            // addOnCameraIdleListener is only invoked when the camera position
            // is changed via .animate(). To handle updating state when .move()
            // is used, it's necessary to set the camera's position here as well
            cameraPositionState.rawPosition = map.cameraPosition
            // Updating user location on every camera move due to lack of a better location updates API.
            cameraPositionState.location = map.locationComponent.lastKnownLocation
        }

        map.addOnMapLongClickListener { point ->
            onMapLongClick?.invoke(point)
            true
        }

        map.addOnCameraMoveCancelListener {
            cameraPositionState.isMoving = false
        }
        map.addOnCameraMoveStartedListener {
            cameraPositionState.cameraMoveStartedReason = CameraMoveStartedReason.fromInt(it)
            cameraPositionState.isMoving = true
        }
        map.addOnCameraMoveListener {
            cameraPositionState.rawPosition = map.cameraPosition
            // Updating user location on every camera move due to lack of a better location updates API.
            cameraPositionState.location = map.locationComponent.lastKnownLocation
        }
        map.locationComponent.addOnCameraTrackingChangedListener(object : OnCameraTrackingChangedListener {
            override fun onCameraTrackingDismissed() {}

            override fun onCameraTrackingChanged(currentMode: Int) {
                cameraPositionState.rawCameraMode = CameraMode.fromInternal(currentMode)
            }
        })
    }

    override fun onRemoved() {
        cameraPositionState.setMap(null)
    }

    override fun onCleared() {
        cameraPositionState.setMap(null)
    }
}

/**
 * Used to keep the primary map properties up to date. This should never leave the map composition.
 */
@SuppressLint("MissingPermission")
@Composable
internal fun MapUpdater(
    cameraPositionState: CameraPositionState,
    locationSettings: MapLocationSettings,
    uiSettings: MapUiSettings,
    symbolManagerSettings: MapSymbolManagerSettings,
    onMapLongClick: ((LatLng) -> Unit)? = null,
) {
    val mapApplier = currentComposer.applier as MapApplier
    val map = mapApplier.map
    val style = mapApplier.style
    val context = LocalContext.current
    ComposeNode<MapPropertiesNode, MapApplier>(
        factory = {
            MapPropertiesNode(
                map = map,
                style = style,
                context = context,
                cameraPositionState = cameraPositionState,
                locationSettings = locationSettings,
                onMapLongClick = onMapLongClick,
            )
        },
        update = {
            set(locationSettings.locationEnabled) {
                map.locationComponent.isLocationComponentEnabled = it
                map.locationComponent.renderMode = RenderMode.COMPASS
            }

            set(uiSettings.compassEnabled) { map.uiSettings.isCompassEnabled = it }
            set(uiSettings.compassMargins) {
                map.uiSettings.setCompassMargins(
                    uiSettings.compassMargins.left,
                    uiSettings.compassMargins.top,
                    uiSettings.compassMargins.right,
                    uiSettings.compassMargins.bottom
                )
            }
            set(uiSettings.rotationGesturesEnabled) { map.uiSettings.isRotateGesturesEnabled = it }
            set(uiSettings.scrollGesturesEnabled) { map.uiSettings.isScrollGesturesEnabled = it }
            set(uiSettings.tiltGesturesEnabled) { map.uiSettings.isTiltGesturesEnabled = it }
            set(uiSettings.zoomGesturesEnabled) { map.uiSettings.isZoomGesturesEnabled = it }
            set(uiSettings.logoGravity) { map.uiSettings.logoGravity = it }
            set(uiSettings.isLogoEnabled) { map.uiSettings.isLogoEnabled = it }
            set(uiSettings.isAttributionEnabled) { map.uiSettings.isAttributionEnabled = it }
            set(uiSettings.attributionGravity) { map.uiSettings.attributionGravity = it }
            set(uiSettings.attributionTintColor) { map.uiSettings.setAttributionTintColor(it.toArgb()) }

            update(cameraPositionState) { this.cameraPositionState = it }
        }
    )
}
