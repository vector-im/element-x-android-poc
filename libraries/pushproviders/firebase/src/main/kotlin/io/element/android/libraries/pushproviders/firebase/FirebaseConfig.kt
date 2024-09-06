/*
 * Copyright 2023, 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package io.element.android.libraries.pushproviders.firebase

object FirebaseConfig {
    /**
     * It is the push gateway for firebase.
     * Note: pusher_http_url should have path '/_matrix/push/v1/notify' -->
     */
    const val PUSHER_HTTP_URL: String = "https://sygnal.daedric.net/_matrix/push/v1/notify"

    const val INDEX = 0
    const val NAME = "Firebase"
}
