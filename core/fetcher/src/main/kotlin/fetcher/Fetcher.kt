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
package fetcher

import fetcher.rss.RssParser
import fetcher.rss.RssParserFactory
import network.NetworkModule
import network.SourceNotFoundException
import network.createNetworkModule

interface Fetcher {
    @Throws(FeedCouldNotBeObtainedException::class)
    suspend fun obtainFeed(url: String): Feed
}

fun createFetcher() = FetcherFactory.create()

internal object FetcherFactory {
    fun create(): Fetcher {
        return NetworkFetcher(
            networkModule = createNetworkModule(),
            parser = RssParserFactory.create(),
        )
    }
}

internal class NetworkFetcher constructor(
    private val networkModule: NetworkModule,
    private val parser: RssParser,
) : Fetcher {
    override suspend fun obtainFeed(url: String): Feed {
        val xml = try {
            networkModule.downloadFileAsText(url)
        } catch (e: SourceNotFoundException) {
            throw FeedNotFoundException(url, e)
        } catch (e: Exception) {
            throw FeedTemporaryUnavailableException(url, e)
        }
        return try {
            parser.parseFeed(xml)
        } catch (e: Exception) {
            throw FeedInvalidException("invalid feed from source: $url", e)
        }
    }
}
