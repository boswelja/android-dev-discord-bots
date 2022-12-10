package fetcher.rss

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.time.OffsetDateTime

@JacksonXmlRootElement(localName = "feed")
internal data class RssFeedDto(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("updated")
    val lastUpdatedOn: OffsetDateTime,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("subtitle")
    val subtitle: String,
    @JsonProperty("author")
    val author: AuthorDto,
    @JsonProperty("generator")
    val generator: String,
    @JsonProperty("totalResults")
    val totalResults: Int,
    @JsonProperty("startIndex")
    val startIndex: Int,
    @JsonProperty("itemsPerPage")
    val itemsPerPage: Int,
    @JacksonXmlElementWrapper(localName = "link", useWrapping = false)
    val link: List<LinkDto>,
    @JacksonXmlElementWrapper(localName = "entry", useWrapping = false)
    val entry: List<RssEntryDto>,
)

internal data class LinkDto(
    @JsonProperty("rel")
    val rel: String,
    @JsonProperty("href")
    val url: String,
    @JsonProperty("type")
    val type: String?,
    @JsonProperty("title")
    val title: String?,
)

internal data class RssEntryDto(
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
    val link: List<LinkDto>,
    @JsonProperty("author")
    val author: AuthorDto,
)

internal data class AuthorDto(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("uri")
    val uri: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("image")
    val profileImage: ProfileImageDto?,
) {
    data class ProfileImageDto(
        @JsonProperty("rel")
        val rel: String,
        @JsonProperty("width")
        val width: Int,
        @JsonProperty("height")
        val height: Int,
        @JsonProperty("src")
        val src: String,
    )
}
