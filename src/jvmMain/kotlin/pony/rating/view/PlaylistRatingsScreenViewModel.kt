package pony.rating.view

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pony.rating.ConfigData
import pony.rating.data.PhonePathLink
import pony.rating.data.PhonePaths
import pony.rating.tools.ConfigUtil
import pony.rating.data.PhonePathsRepository
import pony.rating.data.RatingJobRepository
import pony.rating.di.scope

class PlaylistRatingsScreenViewModel() {

    protected val phonePathsRepository = PhonePathsRepository()
    protected val ratingJobRepository = RatingJobRepository()


    private var configFlow: MutableStateFlow<ConfigData>
    lateinit var state: StateFlow<MainScreenState>

    init {
        val config = ConfigUtil.readConfigFile()
        configFlow = MutableStateFlow(config)
        configFlow
            .drop(1)
            .debounce(500)
            .onEach {
                ConfigUtil.saveConfigFile(it)
            }.launchIn(scope)


        val phonePathsFlow = phonePathsRepository.phonePathsFlow.distinctUntilChanged { old, new ->
            new.paths == old.paths
        }
        val outputLogFlow = ratingJobRepository.outputLogFlow.filterNotNull()
        scope.launch {
            state = combine(
                configFlow,
                phonePathsFlow,
                outputLogFlow,
                ::MainScreenState
            ).stateIn(scope)
        }
    }


    fun onConfigChanged(item: ConfigData) {
        configFlow.value = item
    }

    fun onSdCardSelected(item: PhonePathLink, type: EditTextField) {
        val config = configFlow.value
        val fullPath = item.fullPath + "Music/MusicBee/LunarMusic.m3u8"
        when (type) {
            EditTextField.OldPlaylist -> config.copy(
                playlistPath = fullPath
            )

            EditTextField.NewPlaylist -> config.copy(
                newPlaylistPath = fullPath
            )
        }.run {
            configFlow.value = this
        }
    }

    fun onStartRatingClicked() {
        val config = configFlow.value
        ratingJobRepository.start(config)

    }

    enum class EditTextField {
        OldPlaylist, NewPlaylist
    }

}

data class MainScreenState(
    val config: ConfigData,
    val phonePaths: PhonePaths,
    val jobOutputText: String,
)