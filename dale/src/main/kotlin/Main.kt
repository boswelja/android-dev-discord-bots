import properties.FilePropertiesStore
import studio.AndroidStudioUpdateChecker

suspend fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val propertiesStore = FilePropertiesStore("dale.properties")

    println(AndroidStudioUpdateChecker(propertiesStore).getNewPosts().toString())
}
