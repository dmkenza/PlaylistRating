package pony.rating.view.screen.playlist

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pony.rating.ConfigData
import pony.rating.data.*
import pony.rating.di.scope
import pony.rating.view.screen.ConfigDelegate
import pony.rating.view.screen.ConfigDelegateImpl

class PlaylistRatingsScreenViewModel() : ConfigDelegate by ConfigDelegateImpl() {

    protected val phonePathsRepository = PhonePathsRepository()
    protected val ratingJobRepository = RatingJobRepository()

    lateinit var state: StateFlow<MainScreenState>

    init {
        val configFlow = configRepository.configFlow

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


    fun onStartRatingClicked() {
        val config = state.value.config
        ratingJobRepository.start(config)

    }


}

data class MainScreenState(
    val config: ConfigData,
    val phonePaths: PhonePaths,
    val jobOutputText: String,
)

enum class FieldType {
    OldPlaylist, NewPlaylist, PowerampBackUp
}