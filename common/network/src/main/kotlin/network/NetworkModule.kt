package network


import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

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