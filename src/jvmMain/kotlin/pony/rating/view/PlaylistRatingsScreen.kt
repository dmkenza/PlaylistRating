package pony.rating.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


class PlaylistRatingsScreen : Screen {
    val viewModel = PlaylistRatingsScreenViewModel()

    @Composable
    override fun renderUI() {
        val scope = rememberCoroutineScope()

        val state = viewModel.state.collectAsState()
        val config = state.value.config
        val phonePaths = state.value.phonePaths
        var jobOutputText = state.value.jobOutputText

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

            if(phonePaths.paths.isNotEmpty()){
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                ) {
                    PathsDropdownMenu(phonePaths) { selectedPath ->
                        viewModel.onSdCardSelected(
                            selectedPath,
                            PlaylistRatingsScreenViewModel.EditTextField.OldPlaylist
                        )
                    }
                }
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                value = config.playlistPath,
                onValueChange = {
                    viewModel.onConfigChanged(
                        config.copy(
                            playlistPath = it
                        )
                    )
                },
                label = {
                    Text("m3u8 playlist")
                }
            )

            if(phonePaths.paths.isNotEmpty()){
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                ) {
                    PathsDropdownMenu(phonePaths) { selectedPath ->
                        viewModel.onSdCardSelected(
                            selectedPath,
                            PlaylistRatingsScreenViewModel.EditTextField.NewPlaylist
                        )
                    }
                }
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                value = config.newPlaylistPath,
                onValueChange = {
                    viewModel.onConfigChanged(
                        config.copy(
                            newPlaylistPath = it
                        )
                    )
                },
                label = {
                    Text("new created m3u8 playlist with ratings")
                }
            )

            Column(
                modifier = Modifier.weight(1f).padding(16.dp).fillMaxWidth(),
            ) {
                val scroll = rememberScrollState(0)
                SelectionContainer(
                ) {
                    Text(
                        modifier = Modifier
                            .verticalScroll(scroll),
                        text = jobOutputText
                    )
                }
            }

            Button(
                modifier =
                Modifier.padding(16.dp).height(50.dp),
                onClick = {
                    viewModel.onStartRatingClicked()
                }) {
                Text("Start")
            }
        }

    }
}

