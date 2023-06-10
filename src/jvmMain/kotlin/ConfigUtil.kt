import com.google.gson.Gson
import java.io.File

object ConfigUtil {

    val configFileName = "config.db"

    val gson = Gson()
    val configFile: File

    init {
        File(System.getProperty("user.home") + "\\AppData\\Local\\PlaylistRatings").apply {
            mkdirs()
//            configFile = File( absolutePath + "\\" + configFileName)
            configFile = File(  configFileName)
        }
    }


    fun readConfigFile(): ConfigData = if (configFile.exists()) {
        configFile.readText().run {
            kotlin.runCatching {
                gson.fromJson(this, ConfigData::class.java)
            }.getOrNull() ?: ConfigData()
        }
    } else {
        ConfigData()
    }


    fun saveConfigFile(item: ConfigData) {

        try {
            val json = gson.toJson(item)

            if (!configFile.exists()) {
                configFile.createNewFile()
            }

            configFile.writer().apply {
                append(json)
                flush()
                close()
            }
        } catch (e: Exception) {
            globalError = e.stackTraceToString()
        }

    }
}