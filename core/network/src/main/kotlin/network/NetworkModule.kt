package network


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

interface NetworkModule {
    suspend fun downloadFileAsText(uri: String): String
}

object NetworkModuleFactory {
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
        return try {
            httpClient.get(uri)  {
                expectSuccess = true // I want this to fail if the server return an error
            }.bodyAsText()
        } catch (e: ClientRequestException) {
            throw if (e.response.status == HttpStatusCode.NotFound) {
                SourceNotFoundException(uri, e)
            } else {
                ServerErrorException("Error (${e.response.status}) getting $uri", e)
            }
        } catch (e: ServerResponseException) {
            throw ServerErrorException("Error (${e.response.status}) getting $uri", e)
        } catch (e: Exception) {
            throw NetworkException("Error getting $uri", e)
        }
    }

}