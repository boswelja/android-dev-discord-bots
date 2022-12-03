package rss

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rss.internal.AuthorDto
import rss.internal.LinkDto
import rss.internal.RssEntryDto
import rss.internal.RssFeedDto

interface RssParser {
    suspend fun parseFeed(xml: String): RssFeed
}

class RealRssParser internal constructor(
    private val xmlMapper: ObjectMapper = XmlMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
): RssParser {

    companion object {
        fun create(): RealRssParser {
            return RealRssParser()
        }
    }

    override suspend fun parseFeed(xml: String): RssFeed {
        return withContext(Dispatchers.Default) {
            val dto = parseFeedInternal(xml)
            dto.toRssFeed()
        }
    }

    private fun parseFeedInternal(xml: String): RssFeedDto {
        return xmlMapper.readValue(xml, RssFeedDto::class.java)
    }

    private fun RssFeedDto.toRssFeed(): RssFeed {
        return RssFeed(
            id = id,
            title = title,
            subtitle = subtitle,
            author = author.toAuthor(),
            entries = entry.map { it.toRssEntry() },
            links = link.map { it.toLink() },
            lastUpdatedOn = lastUpdatedOn,
        )
    }

    private fun RssEntryDto.toRssEntry(): RssEntry {
        return RssEntry(
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


