package guildsettings

import kotlinx.coroutines.flow.Flow

interface GuildSettingsDatabase {
    fun getString(guildId: String, key: String): Flow<String?>

    suspend fun setString(guildId: String, key: String, value: String)
}
