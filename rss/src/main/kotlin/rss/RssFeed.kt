package rss

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.time.OffsetDateTime

@JacksonXmlRootElement(localName = "feed")
data class RssFeed(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("updated")
    val lastUpdatedOn: OffsetDateTime,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("subtitle")
    val subtitle: String,
    @JsonProperty("author")
    val author: Author,
    @JsonProperty("generator")
    val generator: String,
    @JsonProperty("totalResults")
    val totalResults: Int,
    @JsonProperty("startIndex")
    val startIndex: Int,
    @JsonProperty("itemsPerPage")
    val itemsPerPage: Int,
    @JacksonXmlElementWrapper(localName = "link", useWrapping = false)
    val link: List<Link>,
    @JacksonXmlElementWrapper(localName = "entry", useWrapping = false)
    val entry: List<RssEntry>
)

data class Link(
    @JsonProperty("rel")
    val rel: String,
    @JsonProperty("href")
    val url: String,
    @JsonProperty("type")
    val type: String?,
    @JsonProperty("title")
    val title: String?
)

data class RssEntry(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("published")
    val publishedOn: OffsetDateTime,
    @JsonProperty("updated")
    val lastUpdatedOn: OffsetDateTime,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("content")
    val content: String,
    @JacksonXmlElementWrapper(localName = "link", useWrapping = false)
    val link: List<Link>,
    @JsonProperty("author")
    val author: Author
)

data class Author(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("uri")
    val uri: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("image")
    val profileImage: ProfileImage?
) {
    data class ProfileImage(
        @JsonProperty("rel")
        val rel: String,
        @JsonProperty("width")
        val width: Int,
        @JsonProperty("height")
        val height: Int,
        @JsonProperty("src")
        val src: String
    )
}
