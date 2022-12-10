package fetcher.rss

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import fetcher.Author
import fetcher.Entry
import fetcher.Feed
import fetcher.Link
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

    private fun parseFeedInternal(xml: String): RssFeedDto {
        return xmlMapper.readValue(xml, RssFeedDto::class.java)
    }

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
