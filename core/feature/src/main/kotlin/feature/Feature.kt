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
package feature

/**
 * A basic interface for defining a "feature". Features must have an entrypoint [init] configured.
 */
interface Feature {

    /**
     * A list of interactions this feature provides. See [Interaction] for details.
     */
    val interactions: List<Interaction>

    /**
     * Initialises the feature. This is a good place to do things like register commands, initialise state based on
     * settings etc.
     */
    fun init()

}
