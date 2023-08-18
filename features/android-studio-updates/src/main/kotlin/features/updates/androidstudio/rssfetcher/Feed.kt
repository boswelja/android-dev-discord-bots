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
package features.updates.androidstudio.rssfetcher

import java.time.OffsetDateTime

// FIXME this should probably be made more generic and moved in another module
// this module should just map between RSS (or a specific RSS version?) to our generic domain objects

/**
 * Represents an RSS Feed.
 *
 * @property id The feed ID.
 * @property title The feed title.
 * @property subtitle A short sentence explaining the purpose of the feed.
 * @property author The [Author] of the feed.
 * @property entries A list of [Entry] written to the feed.
 * @property links A list of [Link]s relevant to the feed.
 * @property lastUpdatedOn An [OffsetDateTime] representing the time the feed was last updated.
 */
data class Feed(
    val id: String,
    val title: String,
    val subtitle: String,
    val author: Author,
    val entries: List<Entry> = emptyList(),
    val links: List<Link> = emptyList(),
    val lastUpdatedOn: OffsetDateTime = OffsetDateTime.now(),
)

/**
 * Represents an entry in an RSS feed.
 *
 * @property id The entry ID.
 * @property title The title of the entry.
 * @property author The entry [Author].
 * @property content The string content of the entry. This may contain HTML formatting, and should be handled as such.
 * @property links A list of [Link]s relevant to the entry.
 * @property publishedOn An [OffsetDateTime] representing the date & time the entry was published.
 */
data class Entry(
    val id: String,
    val title: String,
    val author: Author,
    val content: String,
    val links: List<Link> = emptyList(),
    val publishedOn: OffsetDateTime,
)

/**
 * Represents an author in an RSS feed.
 *
 * @property name The author's full name.
 */
data class Author(
    val name: String,
)

/**
 * Represents a link in an RSS feed.
 *
 * @property url The link URL.
 */
data class Link(
    val url: String,
)
