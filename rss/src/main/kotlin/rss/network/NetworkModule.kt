package rss.network


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

internal interface NetworkModule {
    suspend fun downloadFileAsText(uri: String): String
}

internal object NetworkModuleFactory {
    fun create(): NetworkModule {
        return HttpNetworkModule(
            httpClient = HttpClient(CIO),
        )
    }
}

internal class HttpNetworkModule(
    private val httpClient: HttpClient,
): NetworkModule {
    override suspend fun downloadFileAsText(uri: String): String {
        return httpClient.get(uri).bodyAsText()
    }

}