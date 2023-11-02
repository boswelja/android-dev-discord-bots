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
package features.updates.androidstudio.updatesource.rssfetcher

/**
 * A generic exception thrown when there is an error fetching a Feed.
 */
open class FeedCouldNotBeObtainedException(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message) {
    init {
        if (cause != null) initCause(cause)
    }

    final override fun initCause(cause: Throwable?): Throwable = super.initCause(cause)
}

/**
 * Thrown when a Feed could not be found. For example, when the server returns 404.
 */
class FeedNotFoundException(source: String, cause: Throwable? = null) :
    FeedCouldNotBeObtainedException("Not found at source: $source", cause)

/**
 * Thrown when the feed was found, but could not be parsed. For example, when the URL content was not an RSS feed.
 */
class FeedInvalidException(message: String, cause: Throwable? = null) :
    FeedCouldNotBeObtainedException(message, cause)

/**
 * Thrown when the feed is temporarily unavailable. For example, when the server returns 5xx.
 */
class FeedTemporaryUnavailableException(source: String, cause: Throwable? = null) :
    FeedCouldNotBeObtainedException("Temporary unavailable resource: $source", cause)
