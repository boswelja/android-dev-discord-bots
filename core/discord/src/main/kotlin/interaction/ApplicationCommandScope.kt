package interaction

interface ApplicationCommandScope {
    fun registerGlobalChatInputCommand(builder: ChatInputCommandBuilder.() -> Unit)
}
