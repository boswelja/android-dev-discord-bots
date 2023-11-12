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

/**
 * The base exception thrown when a network-related error occurs.
 */
open class NetworkException(message: String? = null, cause: Throwable? = null) : Exception(message) {
    init {
        if (cause != null) { initCause(cause) }
    }

    final override fun initCause(cause: Throwable?): Throwable = super.initCause(cause)
}

/**
 * Thrown when a resource is not found. For example, when a web server returns 404.
 */
class SourceNotFoundException(source: String, cause: Throwable? = null) :
    NetworkException("Not found: $source", cause)

/**
 * Thrown when the server returns an error, except 404 (see [SourceNotFoundException]).
 */
class ServerErrorException(message: String?, cause: Throwable?) :
    NetworkException(message, cause)
