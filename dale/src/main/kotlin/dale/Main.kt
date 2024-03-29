/*
 * Copyright 2024 AndroidDev Discord Dev Team
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
package dale

import lifecycle.startMainLifecycle

/**
 * The main entrypoint for Dale. Passing a bot token as a program argument is expected.
 */
suspend fun main(args: Array<String>) {
    val apiKey = args.firstOrNull() ?: System.getenv("API_KEY") ?:
    error("No API key was found. Please pass one as your first arg, or via the API_KEY env var")
    startMainLifecycle(DaleBot(apiKey))
}
