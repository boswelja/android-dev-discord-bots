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
