/*
 * Copyright 2023 AndroidDev Discord Dev Team
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
package settings

import kotlinx.coroutines.flow.Flow

/**
 * A generic data store designed to hold key-value settings.
 */
interface BotSettings {

    /**
     * Flows the value of the entry matching the given key.
     */
    fun getString(key: String): Flow<String?>

    /**
     * Sets a value to a key.
     */
    suspend fun setString(key: String, value: String)

    /**
     * Deletes an entry matching the given key.
     */
    suspend fun delete(key: String)

}
