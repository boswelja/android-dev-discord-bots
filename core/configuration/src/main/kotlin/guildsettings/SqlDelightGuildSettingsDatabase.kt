package guildsettings

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import guildsettings.database.GuildSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.sqlite.SQLiteException

internal class SqlDelightGuildSettingsDatabase(
    private val driver: SqlDriver,
) : GuildSettingsDatabase {
    private val database = GuildSettings(driver)

    init {
        try {
            GuildSettings.Schema.create(driver)
        } catch (_: SQLiteException) {
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
