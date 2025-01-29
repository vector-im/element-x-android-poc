/*
 * Copyright 2023, 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.services.analyticsproviders.sentry

import android.content.Context
import com.squareup.anvil.annotations.ContributesMultibinding
import im.vector.app.features.analytics.itf.VectorAnalyticsEvent
import im.vector.app.features.analytics.itf.VectorAnalyticsScreen
import im.vector.app.features.analytics.plan.SuperProperties
import im.vector.app.features.analytics.plan.UserProperties
import io.element.android.libraries.core.meta.BuildMeta
import io.element.android.libraries.core.meta.BuildType
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.di.ApplicationContext
import io.element.android.services.analyticsproviders.api.AnalyticsProvider
import io.element.android.services.analyticsproviders.sentry.log.analyticsTag
import io.sentry.Sentry
import io.sentry.SentryLevel
import io.sentry.SentryOptions
import io.sentry.android.core.SentryAndroid
import timber.log.Timber
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class SentryAnalyticsProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val buildMeta: BuildMeta,
) : AnalyticsProvider {
    override val name = SentryConfig.NAME

    override fun init() {
        Timber.tag(analyticsTag.value).d("Initializing Sentry")
        if (Sentry.isEnabled()) return

        val dsn = if (SentryConfig.DSN.isNotBlank()) {
            SentryConfig.DSN
        } else {
            Timber.w("No Sentry DSN provided, Sentry will not be initialized")
            return
        }

        SentryAndroid.init(context) { options ->
            options.dsn = dsn
            options.beforeSend = SentryOptions.BeforeSendCallback { event, _ -> event }
            options.tracesSampleRate = 1.0
            options.isEnableUserInteractionTracing = true
            options.environment = buildMeta.buildType.toSentryEnv()
        }
    }

    override fun stop() {
        Timber.tag(analyticsTag.value).d("Stopping Sentry")
        Sentry.close()
    }

    override fun capture(event: VectorAnalyticsEvent) {
        Sentry.captureMessage("Event: ${event.getName()}", SentryLevel.INFO)
    }

    override fun screen(screen: VectorAnalyticsScreen) {
        Sentry.captureMessage("Screen: ${screen.getName()}", SentryLevel.INFO)
    }

    override fun updateUserProperties(userProperties: UserProperties) {
    }

    override fun updateSuperProperties(updatedProperties: SuperProperties) {
    }

    override fun trackError(throwable: Throwable) {
        Sentry.captureException(throwable)
    }
}

private fun BuildType.toSentryEnv() = when (this) {
    BuildType.RELEASE -> SentryConfig.ENV_RELEASE
    BuildType.NIGHTLY,
    BuildType.DEBUG -> SentryConfig.ENV_DEBUG
}
