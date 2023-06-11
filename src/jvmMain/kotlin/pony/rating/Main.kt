package pony.rating// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pony.rating.di.commandsGenerator
import pony.rating.view.PlaylistRatingsScreen


fun String.isPhonePath(): Boolean {
    val path = this
    return (path.startsWith("/storage/") || path.startsWith("/sdcard/") || path.startsWith("/mnt/"))


}

fun test1() {
    GlobalScope.launch {

        val path = "/storage/self/primary/Music/MusicBee/playlist.m3u8"

        val cmd = commandsGenerator.adbListVolumes()

        val res = CommandExecutor.execute(cmd)

//        if(res.exitCode != 0){
//            val errorStack =res.error?.stackTraceToString() + ""
//            println("Error with ${res.exitCode} ${res.out} $errorStack")
//        } else {
//            println("Success ${res.out}")
//        }

//        val process = ProcessBuilder(
//            workingFolder + "adbsync.exe",
//            * "push C:\\Users\\Dmitry\\Downloads\\better-adb-sync\\src\\2.txt /storage/417A-1C12/31.txt".split(" ").toTypedArray()
//        ).apply {
//            redirectErrorStream(true)
//        }.start().apply {
//            val out = BufferedReader(InputStreamReader(getInputStream()))
//            var line : String?
//            while (out.readLine().also { line = it } != null) {
//                System.out.println(line)
//            }
//            waitFor()
//        }
//        println(process)
    }

}


@Composable
@Preview
fun App() {

    val screen = PlaylistRatingsScreen()

    test1()
    MaterialTheme {
        screen.renderUI()
    }
}

//#EXT-X-RATING:5
fun main() = application {
    Window(
        title = "Playlist Rating",
        icon = painterResource("ic_luna.svg"),
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
//    val x1 = "C:\\projects\\PlaylistRating\\1.opus".run {
//        File(this).pony.rating.readRatingFromAudioFile()
//    }
//
//    val x2 = "C:\\projects\\PlaylistRating\\1.mp3".run {
//        File(this).pony.rating.readRatingFromAudioFile()
//    }
//    val v1 = "\\Music\\Neighsayer\\Lyra'd\\06 - At Least 1000 Years.mp3"
//    val v2 = "/Music/Neighsayer/Celestia'd/10 - Accepted.opus"
//
//    pony.rating.test1()
//    pony.rating.debug("$x1")
}

