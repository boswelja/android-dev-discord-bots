package rss

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

interface RssParser {
    fun parseFeed(xml: String): RssFeed
}

class RealRssParser (
    private val xmlMapper: ObjectMapper = XmlMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
): RssParser {

    override fun parseFeed(xml: String): RssFeed {
        return xmlMapper.readValue(xml, RssFeed::class.java)
    }

}