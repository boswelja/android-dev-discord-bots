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
package features.updates.androidstudio.updatesource

import fetcher.FeedCouldNotBeObtainedException
import fetcher.Fetcher
import fetcher.FetcherFactory
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant

/**
 * An implementation of [AndroidStudioUpdateSource] that retrieves updates from the Android Studio Blog.
 */
internal class AndroidStudioBlogUpdateSource(
    private val source: Fetcher = FetcherFactory.create(),
) : AndroidStudioUpdateSource {

    override suspend fun getUpdatesAfter(after: Instant): Result<List<AndroidStudioUpdate>> {
        return try {
            val events = source.obtainFeed("https://androidstudio.googleblog.com/feeds/posts/default").entries
                .stream()
                .filter { it.publishedOn.toInstant().toKotlinInstant() > after }
                .map {
                    AndroidStudioUpdate(
                        fullVersionName = extractVersionFromTitle(it.title),
                        summary = it.title, // TODO Can we extract a better summary?
                        timestamp = it.publishedOn.toInstant().toKotlinInstant(),
                        url = it.links.last().url
                    )
                }
            Result.success(events.toList())
        } catch (e: FeedCouldNotBeObtainedException) {
            Result.failure(e)
        }
    }

    internal fun extractVersionFromTitle(title: String): String {
        return when {
            title.contains("is") -> title.split(" is ").first()
            title.contains("now") -> title.split(" now ").first()
            title.contains("available") -> title.split("available").first()
            else -> error("Unknown title format $title")
        }.trim()
    }
}
