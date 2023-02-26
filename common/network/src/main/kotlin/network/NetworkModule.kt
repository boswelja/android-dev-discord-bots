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

fun createNetworkModule() = NetworkModuleFactory.create()

internal object NetworkModuleFactory {
    fun create(): NetworkModule {
        return HttpNetworkModule(
            httpClient = HttpClient(CIO),
        )
    }
}

internal class HttpNetworkModule(
    private val httpClient: HttpClient,
) : NetworkModule {
    override suspend fun downloadFileAsText(uri: String): String {
        return try {
            httpClient.get(uri) {
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
