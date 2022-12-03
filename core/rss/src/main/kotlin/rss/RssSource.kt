package rss

import network.NetworkModule
import network.NetworkModuleFactory
import rss.parser.RssParser
import rss.parser.RssParserFactory

interface RssSource {
    suspend fun obtainRss(url: String): RssFeed
}

object RssSourceFactory {
    fun create(): RssSource {
        return NetworkRssSource(
            networkModule = NetworkModuleFactory.create(),
            parser = RssParserFactory.create(),
        )
    }
}

internal class NetworkRssSource constructor(
    private val networkModule: NetworkModule,
    private val parser: RssParser,
): RssSource {
    override suspend fun obtainRss(url: String): RssFeed {
        // TODO error handling
        val xml = networkModule.downloadFileAsText(url)
        return parser.parseFeed(xml)
    }
}