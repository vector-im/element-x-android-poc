/*
 * Copyright 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.messages.impl.crypto.identity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewParameter
import io.element.android.appconfig.LearnMoreConfig
import io.element.android.compound.theme.ElementTheme
import io.element.android.libraries.designsystem.atomic.molecules.ComposerAlertMolecule
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight
import io.element.android.libraries.matrix.api.encryption.identity.IdentityState
import io.element.android.libraries.ui.strings.CommonStrings

@Composable
fun IdentityChangeStateView(
    state: IdentityChangeState,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Pick the first identity change that is in Pin or Verification violation
    val maybeIdentityChangeViolation = state.roomMemberIdentityStateChanges.firstOrNull {
        it.identityState == IdentityState.PinViolation ||
            it.identityState == IdentityState.VerificationViolation
    }
    if (maybeIdentityChangeViolation != null) {
        ComposerAlertMolecule(
            modifier = modifier,
            avatar = maybeIdentityChangeViolation.identityRoomMember.avatarData,
            content = buildAnnotatedString {
                val learnMoreStr = stringResource(CommonStrings.action_learn_more)
                val displayName = maybeIdentityChangeViolation.identityRoomMember.displayNameOrDefault
                val userIdStr = stringResource(
                    CommonStrings.crypto_identity_change_pin_violation_new_user_id,
                    maybeIdentityChangeViolation.identityRoomMember.userId,
                )

                val fullText = if (maybeIdentityChangeViolation.identityState == IdentityState.PinViolation) {
                    stringResource(
                        id = CommonStrings.crypto_identity_change_pin_violation_new,
                        displayName,
                        userIdStr,
                        learnMoreStr,
                    )
                } else {
                    stringResource(
                        id = CommonStrings.crypto_identity_change_verification_violation_new,
                        displayName,
                        userIdStr,
                        learnMoreStr,
                    )
                }
                append(fullText)
                val userIdStartIndex = fullText.indexOf(userIdStr)
                addStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                    ),
                    start = userIdStartIndex,
                    end = userIdStartIndex + userIdStr.length,
                )
                val learnMoreStartIndex = fullText.lastIndexOf(learnMoreStr)
                addStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        color = ElementTheme.colors.textPrimary
                    ),
                    start = learnMoreStartIndex,
                    end = learnMoreStartIndex + learnMoreStr.length,
                )
                addLink(
                    url = LinkAnnotation.Url(
                        url = LearnMoreConfig.IDENTITY_CHANGE_URL,
                        linkInteractionListener = {
                            onLinkClick(LearnMoreConfig.IDENTITY_CHANGE_URL)
                        }
                    ),
                    start = learnMoreStartIndex,
                    end = learnMoreStartIndex + learnMoreStr.length,
                )
            },
            submitText = if (maybeIdentityChangeViolation.identityState == IdentityState.VerificationViolation) {
                stringResource(CommonStrings.crypto_identity_change_withdraw_verification_action)
            } else {
                stringResource(CommonStrings.action_ok)
            },
            onSubmitClick = {
                if (maybeIdentityChangeViolation.identityState == IdentityState.VerificationViolation) {
                    state.eventSink(IdentityChangeEvent.WithdrawVerification(maybeIdentityChangeViolation.identityRoomMember.userId))
                } else {
                    state.eventSink(IdentityChangeEvent.PinIdentity(maybeIdentityChangeViolation.identityRoomMember.userId))
                }
            },
            isCritical = maybeIdentityChangeViolation.identityState == IdentityState.VerificationViolation,
        )
    }
}

@PreviewsDayNight
@Composable
internal fun IdentityChangeStateViewPreview(
    @PreviewParameter(IdentityChangeStateProvider::class) state: IdentityChangeState,
) = ElementPreview {
    IdentityChangeStateView(
        state = state,
        onLinkClick = {},
    )
}
