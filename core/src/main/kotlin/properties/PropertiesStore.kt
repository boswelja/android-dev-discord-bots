package properties

interface PropertiesStore {
    operator fun get(key: String): Any?

    operator fun set(key: String, value: Any?)
}
