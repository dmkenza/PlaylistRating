package pony.rating.view.screen.backsync

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pony.rating.ConfigData
import pony.rating.data.ConfigRepository
import pony.rating.data.PhonePaths
import pony.rating.data.PhonePathsRepository
import pony.rating.data.RatingJobRepository
import pony.rating.di.scope
import pony.rating.tools.ConfigUtil
import pony.rating.view.screen.ConfigDelegate
import pony.rating.view.screen.ConfigDelegateImpl
import pony.rating.view.screen.playlist.MainScreenState

class BackSyncScreenViewModel : ConfigDelegate by ConfigDelegateImpl() {

    protected val phonePathsRepository = PhonePathsRepository()

    lateinit var state: StateFlow<BackSyncScreenState>

    init {

        val configFlow = configRepository.configFlow

        val phonePathsFlow = phonePathsRepository.phonePathsFlow.distinctUntilChanged { old, new ->
            new.paths == old.paths
        }
        scope.launch {
            state = combine(
                configFlow,
                phonePathsFlow,
                ::BackSyncScreenState
            ).stateIn(scope)
        }
    }

}

data class BackSyncScreenState(
    val config: ConfigData,
    val phonePaths: PhonePaths,
)