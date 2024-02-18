/*
 * Copyright 2024 AndroidDev Discord Dev Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package features.updates.androidstudio.updatesource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * A source for Android Studio updates.
 */
interface AndroidStudioUpdateSource {

    /**
     * Flows the latest stable release of Android Studio.
     */
    val latestStableUpdate: StateFlow<AndroidStudioUpdate?>

    /**
     * Flows the latest RC release of Android Studio.
     */
    val latestReleaseCandidateUpdate: StateFlow<AndroidStudioUpdate?>

    /**
     * Flows the latest beta release of Android Studio.
     */
    val latestBetaUpdate: StateFlow<AndroidStudioUpdate?>

    /**
     * Flows the latest canary release of Android Studio.
     */
    val latestCanaryUpdate: StateFlow<AndroidStudioUpdate?>

    /**
     * Emits any new Android Studio releases to subscribers.
     */
    val latestUpdate: Flow<AndroidStudioUpdate>

    /**
     * checks for any new updates and submits them to the respective Flows.
     */
    suspend fun checkForUpdates()
}
