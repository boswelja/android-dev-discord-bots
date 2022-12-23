package interaction

interface ChatInputCommandBuilder : CommandBuilder {

    fun subCommand(name: String, description: String, builder: SubCommandBuilder.() -> Unit)

    fun subCommandGroup(name: String, description: String, builder: SubCommandGroupBuilder.() -> Unit)
}

interface CommandBuilder {
    fun int(name: String, description: String, required: Boolean)

    fun string(name: String, description: String, required: Boolean)

    fun boolean(name: String, description: String, required: Boolean)

    fun user(name: String, description: String, required: Boolean)

    fun channel(name: String, description: String, required: Boolean)

    fun mentionable(name: String, description: String, required: Boolean)

    fun number(name: String, description: String, required: Boolean)
}

interface SubCommandBuilder : CommandBuilder

interface SubCommandGroupBuilder {
    fun subCommand(name: String, description: String, builder: SubCommandBuilder.() -> Unit)
}
