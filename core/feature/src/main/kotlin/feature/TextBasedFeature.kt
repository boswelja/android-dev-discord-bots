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
package feature

/**
 * A [Feature] that is driven by text-based interactions.
 */
interface TextBasedFeature: Feature {

    /**
     * A list of interactions this feature provides. See [Interaction] for details.
     */
    val interactions: List<Interaction>

}

/**
 * Allows the bot to respond to interactions.
 */
sealed interface ResponseScope {

    /**
     * Acknowledges the interaction. This provides an indicator to users that the bot has received their input.
     */
    suspend fun acknowledge()

    /**
     * Responds to the interaction with a plaintext message.
     */
    suspend fun respond(text: String)
}

/**
 * An [Interaction] that is purely text-based, i.e. triggered via text input.
 */
sealed interface TextBasedInteraction : Interaction

/**
 * Defines a group of [ChatInteractionGroupItem]s that the user can interact with. Users will need to invoke [groupName]
 * followed by the desired [ChatInteractionGroupItem.name] to invoke any interactions.
 *
 * @property name The name of the group of interactions. Users will need to enter this as a prefix for any
 * interactions contained in this group.
 * @property description A short sentence explaining the purpose of interactions within this group.
 * @property subcommands A list of [ChatInteractionGroupItem] that users can invoke.
 * @property permissionLevel The [Interaction.PermissionLevel] that users must have to be able to invoke this
 * interaction.
 */
data class ChatInteractionGroup(
    val name: String,
    val description: String,
    val subcommands: List<ChatInteractionGroupItem>,
    val permissionLevel: Interaction.PermissionLevel,
) : TextBasedInteraction


/**
 * The base type for all interaction parameters. A parameter is a value that users can input alongside the
 * interaction.
 *
 * @property name The name of the parameter. Users will typically need to type this before entering a value.
 * @property description A short sentence to explain what the parameter is used for.
 * @property required Whether the parameter is required for the interaction.
 * @property type The type of value expected from the user. See [Type] for all possible values
 */
data class Parameter(
    val name: String,
    val description: String,
    val required: Boolean,
    val type: Type
) {
    /**
     * Defines all possible types for [Parameter] values.
     */
    enum class Type {
        String,
        Bool,
        Channel
    }
}
/**
 * Describes an interaction contained within a [ChatInteractionGroup] that users can interact with.
 *
 * @property name The name of the interaction. Users will need to enter [ChatInteractionGroup.name] followed by
 * this to invoke the interaction.
 * @property description A short sentence explaining what this interaction does.
 * @property parameters A list of [Parameter]s that this interaction accepts.
 * @property onInvoke Called when a user successfully invokes this interaction. A Map of parameter names to their values
 * is provided.
 */
data class ChatInteractionGroupItem(
    val name: String,
    val description: String,
    val parameters: List<Parameter>,
    val onInvoke: ResponseScope.(parameters: Map<String, Any>) -> Unit,
)

/**
 * Describes an interaction that users can interact with.
 *
 * @property name The name of the interaction. Users will need to enter [ChatInteractionGroup.groupName] followed by
 * this to invoke the interaction.
 * @property description A short sentence explaining what this interaction does.
 * @property parameters A list of [Interaction.Parameter] that this interaction accepts.
 * @property permissionLevel The [Interaction.PermissionLevel] that users must have to be able to invoke this
 * interaction.
 * @property onInvoke Called when a user successfully invokes this interaction. A Map of parameter names to their values
 * is provided.
 */
data class ChatInteraction(
    val name: String,
    val description: String,
    val parameters: List<Parameter>,
    val permissionLevel: Interaction.PermissionLevel,
    val onInvoke: ResponseScope.(parameters: Map<String, Any>) -> Unit,
) : TextBasedInteraction
