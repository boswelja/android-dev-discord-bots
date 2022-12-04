package guildsettings

object GuildSettingsRepositoryFactory {
    private val instances: MutableMap<String, GuildSettingsRepository> = mutableMapOf()

    fun instance(name: String): GuildSettingsRepository {
        return instances[name] ?: synchronized(this) {
            instances[name]
                ?: SqlDelightGuildSettingsRepository("$name.db")
                    .also { instances[name] = it }
        }
    }
}