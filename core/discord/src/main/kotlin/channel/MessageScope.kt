package channel

interface MessageScope {
    fun createEmbed(targetChannelId: String, builder: EmbedBuilder.() -> Unit)
}
