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
package fetcher

import java.time.OffsetDateTime

// FIXME this should probably be made more generic and moved in another module
// this module should just map between RSS (or a specific RSS version?) to our generic domain objects

data class Feed(
    val id: String,
    val title: String,
    val subtitle: String,
    val author: Author,
    val entries: List<Entry> = emptyList(),
    val links: List<Link> = emptyList(),
    val lastUpdatedOn: OffsetDateTime = OffsetDateTime.now(),
)

data class Entry(
    val id: String,
    val title: String,
    val author: Author,
    val content: String,
    val links: List<Link> = emptyList(),
    val publishedOn: OffsetDateTime,
)

data class Author(
    val name: String,
)

data class Link(
    val url: String,
)
