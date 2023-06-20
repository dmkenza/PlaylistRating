package pony.rating.data

import kotlinx.coroutines.flow.*
import pony.rating.ConfigData
import pony.rating.di.scope
import pony.rating.tools.ConfigUtil

class ConfigRepository {

    var configFlow: MutableStateFlow<ConfigData>

    init {
        val config = ConfigUtil.readConfigFile()
        configFlow = MutableStateFlow(config)
        configFlow
            .drop(1)
            .debounce(500)
            .onEach {
                ConfigUtil.saveConfigFile(it)
            }.launchIn(scope)
    }

    fun onChange(item: ConfigData) {
        configFlow.value = item
    }
}