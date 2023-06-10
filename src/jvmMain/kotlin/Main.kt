// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.flow.*
import org.chenliang.oggus.opus.OggOpusStream
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File

@Composable
@Preview
fun App() {

    val config = ConfigUtil.readConfigFile()
    val configFlow = MutableStateFlow(
        config
    )

    var organizedMusicFolderPathText by remember {
        mutableStateOf(config.organizedMusicFolderPath)
    }
    var playlistPath by remember {
        mutableStateOf(config.playlistPath)
    }
    var newPlaylistPath by remember {
        mutableStateOf(config.newPlaylistPath)
    }

    var logText by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    configFlow
        .drop(1)
        .debounce(500)
        .onEach {
            ConfigUtil.saveConfigFile(it)
        }.launchIn(scope)


    MaterialTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                value = organizedMusicFolderPathText,
                onValueChange = {
                    configFlow.value = configFlow.value.copy(
                        organizedMusicFolderPath = it
                    )
                    organizedMusicFolderPathText = it
                },
                label = {
                    Text("Organized Music Folder")
                }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                value = playlistPath,
                onValueChange = {
                    configFlow.value = configFlow.value.copy(
                        playlistPath = it
                    )
                    playlistPath = it
                },
                label = {
                    Text("m3u8 playlist")
                }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                value = newPlaylistPath,
                onValueChange = {
                    configFlow.value = configFlow.value.copy(
                        newPlaylistPath = it
                    )
                    newPlaylistPath = it
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
                        text = logText
                    )
                }
            }

            Button(
                modifier =
                Modifier.padding(16.dp).height(50.dp),
                onClick = {
                    createRatingJob(scope, configFlow.value){
                        logText = globalError + it
                    }

                }) {
                Text("Start")
            }
        }

    }
}

//#EXT-X-RATING:5
fun main() = application {
    Window(
        title = "Playlist Rating",
        icon = painterResource("ic_luna.svg"),
        onCloseRequest = ::exitApplication) {
        App()
    }
//    val x1 = "C:\\projects\\PlaylistRating\\1.opus".run {
//        File(this).readRatingFromAudioFile()
//    }
//
//    val x2 = "C:\\projects\\PlaylistRating\\1.mp3".run {
//        File(this).readRatingFromAudioFile()
//    }
//    val v1 = "\\Music\\Neighsayer\\Lyra'd\\06 - At Least 1000 Years.mp3"
//    val v2 = "/Music/Neighsayer/Celestia'd/10 - Accepted.opus"
//
//    test1()
//    debug("$x1")
}

