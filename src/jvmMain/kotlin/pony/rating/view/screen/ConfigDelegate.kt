package pony.rating.view.screen

import pony.rating.ConfigData
import pony.rating.data.ConfigRepository
import pony.rating.data.PhonePathLink
import pony.rating.view.screen.playlist.FieldType

interface ConfigDelegate{
    val configRepository : ConfigRepository
    fun onConfigChanged(item: ConfigData)
    fun onSdCardSelected(item: PhonePathLink, type: FieldType)
}

class ConfigDelegateImpl () : ConfigDelegate {
    override val configRepository = ConfigRepository()

    override fun onConfigChanged(item: ConfigData) {
        configRepository.onChange(item)
    }

    override fun onSdCardSelected(item: PhonePathLink, type: FieldType) {
        val config = configRepository.configFlow.value
        var fullPath = item.fullPath + "Music/MusicBee/LunarMusic.m3u8"
        when (type) {
            FieldType.OldPlaylist -> config.copy(
                playlistPath = fullPath
            )

            FieldType.NewPlaylist -> config.copy(
                newPlaylistPath = fullPath
            )

            FieldType.PowerampBackUp -> {
                fullPath = item.fullPath + "Music/.poweramp-backup"
                config.copy(
                    powerampBackUpPath = fullPath
                )
            }
        }.run {
            configRepository.onChange(this)
        }
    }
}