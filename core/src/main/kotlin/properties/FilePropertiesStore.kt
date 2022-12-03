package properties

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer
import java.util.Properties

class FilePropertiesStore(
    fileName: String
) : PropertiesStore {
    private val fileWriter: Writer
    private val properties: Properties

    init {
        val file = File("config", fileName)
        if (!file.exists()) {
            file.parentFile.mkdir()
            file.createNewFile()
        }
        properties = Properties()
        FileReader(file).use {
            properties.load(it)
        }
        fileWriter = FileWriter(file)
    }

    override fun get(key: String): Any? {
        return properties[key]
    }

    override fun set(key: String, value: Any?) {
        properties[key] = value
        properties.store(fileWriter, null)
    }
}