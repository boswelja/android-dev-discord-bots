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