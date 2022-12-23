package channel

import kotlinx.datetime.Instant

interface EmbedBuilder {
    var title: String?
    var description: String?
    var url: String?
    var timestamp: Instant
    var color: Int

    fun footer(text: String, iconUrl: String?, proxyIconUrl: String?)
    fun image(url: String, proxyUrl: String?, height: Int?, width: Int?)
    fun thumbnail(url: String, proxyUrl: String?, height: Int?, width: Int?)
    fun video(url: String, proxyUrl: String?, height: Int?, width: Int?)
    fun provider(name: String?, url: String?)
    fun author(name: String, url: String?, iconUrl: String?, proxyIconUrl: String?)
}
