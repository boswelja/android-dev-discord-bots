package guildsettings

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import guildsettings.database.GuildSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.sqlite.SQLiteException

class GuildSettingsRepositoryImpl(
    fileName: String
) : GuildSettingsRepository {
    private val driver = JdbcSqliteDriver("jdbc:sqlite:$fileName")
    private val database = GuildSettings(driver)

    init {
        try {
            GuildSettings.Schema.create(driver)
        } catch (e: SQLiteException) {
            // This probably means the table already exists, so ignore exceptions
        }
    }

    override fun getString(guildId: String, key: String): Flow<String?> {
        return database.guildSettingsQueries.get(guildId, key).asFlow().mapToOneOrNull()
    }

    override suspend fun setString(guildId: String, key: String, value: String) {
        withContext(Dispatchers.IO) {
            database.guildSettingsQueries.set(guildId, key, value)
        }
    }
}
