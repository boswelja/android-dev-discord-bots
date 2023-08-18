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
package features.updates.androidstudio.rssfetcher.rss

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import features.updates.androidstudio.rssfetcher.Author
import features.updates.androidstudio.rssfetcher.Entry
import features.updates.androidstudio.rssfetcher.Feed
import features.updates.androidstudio.rssfetcher.Link
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface RssParser {
    suspend fun parseFeed(xml: String): Feed
}

internal object RssParserFactory {
    fun create(): RssParser {
        return RealRssParser(
            xmlMapper = XmlMapper()
                .registerKotlinModule()
                .registerModule(JavaTimeModule()),
        )
    }
}

internal class RealRssParser constructor(
    private val xmlMapper: ObjectMapper,
) : RssParser {

    override suspend fun parseFeed(xml: String): Feed {
        return withContext(Dispatchers.Default) {
            val dto = parseFeedInternal(xml)
            dto.toRssFeed()
        }
    }

    private fun parseFeedInternal(xml: String): RssFeedDto = xmlMapper.readValue(xml, RssFeedDto::class.java)

    private fun RssFeedDto.toRssFeed(): Feed {
        return Feed(
            id = id,
            title = title,
            subtitle = subtitle,
            author = author.toAuthor(),
            entries = entry.map { it.toRssEntry() },
            links = link.map { it.toLink() },
            lastUpdatedOn = lastUpdatedOn,
        )
    }

    private fun RssEntryDto.toRssEntry(): Entry {
        return Entry(
            id = id,
            title = title,
            author = author.toAuthor(),
            content = content,
            links = link.map { it.toLink() },
            publishedOn = publishedOn,
        )
    }

    private fun AuthorDto.toAuthor(): Author {
        return Author(
            name = name,
        )
    }

    private fun LinkDto.toLink(): Link {
        return Link(
            url = url,
        )
    }
}
