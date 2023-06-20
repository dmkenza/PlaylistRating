package pony.rating.view.screen.backsync

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pony.rating.view.PathsDropdownMenu
import pony.rating.view.screen.Screen
import pony.rating.view.screen.playlist.FieldType

class BackSyncScreen () :Screen{

    val viewModel = BackSyncScreenViewModel()

    @Composable
    override fun renderUI() {
        val state = viewModel.state.collectAsState()
        val config = state.value.config
        val phonePaths = state.value.phonePaths


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                value = config.organizedMusicFolderPath,
                onValueChange = {
                    viewModel.onConfigChanged(
                        config.copy(
                            organizedMusicFolderPath = it
                        )
                    )
                },
                label = {
                    Text("Organized Music Folder")
                }
            )

            if (phonePaths.paths.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                ) {
                    PathsDropdownMenu(phonePaths) { selectedPath ->
                        viewModel.onSdCardSelected(
                            selectedPath,
                            FieldType.PowerampBackUp
                        )
                    }
                }
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                value = config.powerampBackUpPath,
                onValueChange = {
                    viewModel.onConfigChanged(
                        config.copy(
                            powerampBackUpPath = it
                        )
                    )
                },
                label = {
                    Text("Poweramp Backup")
                }
            )

        }

    }

}