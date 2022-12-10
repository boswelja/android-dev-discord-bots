package sqldelight

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

internal object SQLDelightDrivers {
    private val instances: MutableMap<String, SqlDriver> = mutableMapOf()

    fun instance(name: String): SqlDriver {
        return instances[name] ?: synchronized(this) {
            File("dbs").mkdirs()
            instances[name]
                ?: JdbcSqliteDriver("jdbc:sqlite:dbs/$name.db")
                    .also { instances[name] = it }
        }
    }
}
