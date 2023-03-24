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

package io.element.android.libraries.matrix.test

import io.element.android.libraries.matrix.api.MatrixClient
import io.element.android.libraries.matrix.api.core.RoomId
import io.element.android.libraries.matrix.api.core.SessionId
import io.element.android.libraries.matrix.api.core.UserId
import io.element.android.libraries.matrix.api.media.MediaResolver
import io.element.android.libraries.matrix.api.room.MatrixRoom
import io.element.android.libraries.matrix.api.room.RoomSummaryDataSource
import io.element.android.libraries.matrix.api.verification.SessionVerificationService
import io.element.android.libraries.matrix.test.media.FakeMediaResolver
import io.element.android.libraries.matrix.test.room.FakeMatrixRoom
import io.element.android.libraries.matrix.test.room.FakeRoomSummaryDataSource
import io.element.android.libraries.matrix.test.verification.FakeSessionVerificationService
import kotlinx.coroutines.delay

class FakeMatrixClient(
    override val sessionId: SessionId = A_SESSION_ID,
    private val userDisplayName: Result<String> = Result.success(A_USER_NAME),
    private val userAvatarURLString: Result<String> = Result.success(AN_AVATAR_URL),
    override val roomSummaryDataSource: RoomSummaryDataSource = FakeRoomSummaryDataSource(),
    private val sessionVerificationService: FakeSessionVerificationService = FakeSessionVerificationService()
) : MatrixClient {

    private var createDmResult: Result<RoomId> = Result.success(A_ROOM_ID)
    private var findDmResult: MatrixRoom? = FakeMatrixRoom()
    private var logoutFailure: Throwable? = null

    override fun getRoom(roomId: RoomId): MatrixRoom? {
        return FakeMatrixRoom(roomId)
    }

    override suspend fun createDM(userId: UserId): Result<RoomId> {
        return createDmResult
    }

    override fun findDM(userId: UserId): MatrixRoom? {
        return findDmResult
    }

    override fun startSync() = Unit

    override fun stopSync() = Unit

    override fun mediaResolver(): MediaResolver {
        return FakeMediaResolver()
    }

    override suspend fun logout() {
        delay(100)
        logoutFailure?.let { throw it }
    }

    override fun close() = Unit

    override suspend fun loadUserDisplayName(): Result<String> {
        return userDisplayName
    }

    override suspend fun loadUserAvatarURLString(): Result<String?> {
        return userAvatarURLString
    }

    override suspend fun loadMediaContent(url: String): Result<ByteArray> {
        return Result.success(ByteArray(0))
    }

    override suspend fun loadMediaThumbnail(url: String, width: Long, height: Long): Result<ByteArray> {
        return Result.success(ByteArray(0))
    }

    override fun sessionVerificationService(): SessionVerificationService = sessionVerificationService

    override fun onSlidingSyncUpdate() {}

    // Mocks

    fun givenLogoutError(failure: Throwable) {
        logoutFailure = failure
    }

    fun givenCreateDmResult(result: Result<RoomId>) {
        createDmResult = result
    }

    fun givenFindDmResult(result: MatrixRoom?) {
        findDmResult = result
    }
}
