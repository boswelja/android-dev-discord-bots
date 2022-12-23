/*
 * Copyright 2022 AndroidDev Discord Dev Team
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
package channel

import kotlinx.datetime.Instant

interface EmbedBuilder {
    var title: String?
    var description: String?
    var url: String?
    var timestamp: Instant
    var color: Int

    fun footer(text: String, iconUrl: String?, proxyIconUrl: String?)
    fun image(url: String, proxyUrl: String?, height: Int?, width: Int?)
    fun thumbnail(url: String, proxyUrl: String?, height: Int?, width: Int?)
    fun video(url: String, proxyUrl: String?, height: Int?, width: Int?)
    fun provider(name: String?, url: String?)
    fun author(name: String, url: String?, iconUrl: String?, proxyIconUrl: String?)
}
