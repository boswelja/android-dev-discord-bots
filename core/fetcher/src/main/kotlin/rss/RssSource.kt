package rss

import network.NetworkModule
import network.NetworkModuleFactory
import network.SourceNotFoundException
import rss.parser.RssParser
import rss.parser.RssParserFactory

interface RssSource {
    @Throws(RssCouldNotBeObtainedException::class)
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
        val xml = try {
            networkModule.downloadFileAsText(url)
        } catch (e: SourceNotFoundException) {
            throw RssNotFoundException(url, e)
        } catch (e: Exception) {
            throw RssTemporaryUnavailableException(url, e)
        }
        return try {
            parser.parseFeed(xml)
        } catch (e: Exception) {
            throw RssInvalidException("invalid feed from source: $url", e)
        }
    }
}