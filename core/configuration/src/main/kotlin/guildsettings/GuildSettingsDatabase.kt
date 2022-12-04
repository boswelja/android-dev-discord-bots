package guildsettings

import kotlinx.coroutines.flow.Flow

// TODO write higher level functions to alter the DB and expose those
interface GuildSettingsDatabase {
    fun getString(guildId: String, key: String): Flow<String?>

    suspend fun setString(guildId: String, key: String, value: String)
}
