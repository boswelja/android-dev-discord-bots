package guildsettings

import sqldelight.SQLDelightDrivers

object GuildSettingsDatabaseFactory {
    private val instances: MutableMap<String, GuildSettingsDatabase> = mutableMapOf()

    fun instance(name: String): GuildSettingsDatabase {
        return instances[name] ?: synchronized(this) {
            instances[name]
                ?: SqlDelightGuildSettingsDatabase(SQLDelightDrivers.instance(name))
                    .also { instances[name] = it }
        }
    }
}