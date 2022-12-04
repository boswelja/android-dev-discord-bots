package fetcher

import network.NetworkModule
import network.NetworkModuleFactory
import network.SourceNotFoundException
import fetcher.rss.RssParser
import fetcher.rss.RssParserFactory

interface Fetcher {
    @Throws(FeedCouldNotBeObtainedException::class)
    suspend fun obtainFeed(url: String): Feed
}

object FetcherFactory {
    fun create(): Fetcher {
        return NetworkFetcher(
            networkModule = NetworkModuleFactory.create(),
            parser = RssParserFactory.create(),
        )
    }
}

internal class NetworkFetcher constructor(
    private val networkModule: NetworkModule,
    private val parser: RssParser,
): Fetcher {
    override suspend fun obtainFeed(url: String): Feed {
        val xml = try {
            networkModule.downloadFileAsText(url)
        } catch (e: SourceNotFoundException) {
            throw FeedNotFoundException(url, e)
        } catch (e: Exception) {
            throw FeedTemporaryUnavailableException(url, e)
        }
        return try {
            parser.parseFeed(xml)
        } catch (e: Exception) {
            throw FeedInvalidException("invalid feed from source: $url", e)
        }
    }
}