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
package logging

import io.github.oshai.KLogger
import io.github.oshai.KotlinLogging

private val loggers = mutableMapOf<String, KLogger>()

private fun getOrCreateLogger(name: String): KLogger = loggers.getOrPut(name) { KotlinLogging.logger(name) }

fun Any.logDebug(throwable: Throwable? = null, message: () -> String) {
    getOrCreateLogger(javaClass.name).debug(throwable, message)
}

fun Any.logInfo(throwable: Throwable? = null, message: () -> String) {
    getOrCreateLogger(javaClass.name).info(throwable, message)
}

fun Any.logWarn(throwable: Throwable? = null, message: () -> String) {
    getOrCreateLogger(javaClass.name).warn(throwable, message)
}

fun Any.logError(throwable: Throwable? = null, message: () -> String) {
    getOrCreateLogger(javaClass.name).error(throwable, message)
}
