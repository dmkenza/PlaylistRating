// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.*
import org.apache.commons.io.FileUtils
import org.chenliang.oggus.opus.OggOpusStream
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.ogg.OggFileWriter
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.id3.ID3v23Frame
import org.jaudiotagger.tag.id3.ID3v23Tag
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTagField
import org.jetbrains.exposed.sql.*
import pony.rating.Command
import pony.rating.CommandExecutor
import pony.rating.CommandsGenerator
import pony.rating.di.commandsGenerator
import pony.rating.di.scope
import pony.rating.tools.findSongPath
import pony.rating.tools.readRatingFromAudioFile
import pony.rating.view.screen.NavigationScreen
import java.io.DataInputStream
import java.io.File
import java.net.InetAddress
import java.net.ServerSocket


object Tracks : Table() {
    //    val id = varchar("id", 10) // Column<String>
    val id = integer("_id").autoIncrement()
    val name = varchar("path", length = 50) // Column<String>
    val rating = varchar("rating", length = 50) // Column<String>
//    val cityId = (integer("city_id") references Cities.id).nullable() // Column<Int?>

    override val primaryKey = PrimaryKey(id, name = "PK_User_ID") // name is optional here
}


val listMusicToCutting = listOf<Triple<String, String, String>> (

//    Triple("C:\\Users\\Dmitry\\Downloads\\H8 Seed - Daring Mare (Likonan Remix).mp3",
//        "0:00.000",
//        "4:08.500"
//    ),


    )


data class TripleK(
    val x1: String,
    val x2: String,
    val x3: String,
)


suspend fun cutMusic() {
    listMusicToCutting.map {
        val (mp3, start, end) = it
        FFMPEG_cut(mp3.replace(" ", "%20"), start, end)
    }
}


// FFMPEG -ss 00:00:00 -i 1.mp3 -c copy -t 3:34.120 1.mp3
suspend fun FFMPEG_cut(
    audioPath: String,
    startDuration: String,
    endDuration: String,
) {
    val path = audioPath.replace("%20", " ")
    FileUtils.copyFile(File(path), File("C:/covers/" + File(path).name));

//    val startDuration = "00:00:00"
//    val endDuration = "3:34.120"
//    val audioPath = "C:\\Users\\Dmitry\\Music\\MusicBee\\Music\\Belgerum\\Unknown Album\\Slender Pony.mp3".replace(" ", "%20")
//    val args = "-ss $startDuration -i $audioPath -c copy -t $endDuration $audioPath"

    val tempFile = File.createTempFile("qwerty", "." + File(audioPath).extension).absolutePath
    val tempFileCover = File("C:/covers/" + File(audioPath).nameWithoutExtension + ".jpg").absolutePath.run {
        "\"$this\""
    }


    File("C:\\covers").mkdirs()
    val x1 = runCatching {
        AudioFileIO.read(File(audioPath.replace("%20", " "))).tag.getFirstField(FieldKey.COVER_ART)
    }.getOrNull()

    val audioPath2 = audioPath.run {
        "\"$this\"".replace("Alone", "test_alone")
    }

    val audioPath = audioPath.run {
        "\"$this\""
    }



    val args3 = "-i -y $audioPath -an -vcodec copy $tempFileCover"
    val cmd3 = Command(
        workingFolder = "",
        exeFile = "ffmpeg",
        args = args3
    )
//    val res3 = GlobalScope.async {
//        CommandExecutor.execute(cmd3)
//    }.await()


    val coverCmd = if(x1 != null){
        "ffmpeg -i $audioPath -an -vcodec copy $tempFileCover -y &&"
    }else{
        ""
    }

    val args2 =
        "/c $coverCmd ffmpeg -y -i $audioPath -ss $startDuration -c copy -t $endDuration $tempFile && move /Y $tempFile $audioPath"
    val cmd = Command(
        workingFolder = "",
        exeFile = "cmd",
        args = args2
    )

    x1

    val res = scope.async(Dispatchers.IO) {
        CommandExecutor.execute(cmd)
    }.await()

    if (res.exitCode != 0) {
        val errorStack = res.error?.stackTraceToString() + ""
        println("Error with ${res.exitCode} ${res.out} $errorStack")
        if (res.error != null) {
            throw res.error
        } else {
            throw Exception("Error with ${res.exitCode} ${res.out}")
        }
    }

//    val args4 =
//        ""
//    val cmd4 = Command(
//        workingFolder = "",
//        exeFile = "cmd",
//        args = args4
//    )
//
//    val res4 = scope.async(Dispatchers.IO) {
//        CommandExecutor.execute(cmd4)
//    }.await()

}

fun test1() {
    GlobalScope.launch {

        //cutMusic()
        return@launch

        val x = "/Music/MusicBee/Resonant Waves/Life in Equestria Vol.2/Zecora's Safe Haven.opus".findSongPath()
        println(x)

//        val serverSocket = ServerSocket(5555)
//
//        val sockaddr = serverSocket.inetAddress
//        val addr = sockaddr.getAddress()
//
//
//        val IP = InetAddress.getLocalHost()
//        System.out.println("IP of my system is := " + IP.hostAddress)
//        serverSocket.localSocketAddress.toString()
//
//        System.out.println(sockaddr.hostAddress)
//        System.out.println(addr)
//        //System.out.println(sockaddr.getPort())
//
//
//        val socket = serverSocket.accept()
//        val dataInputStream = DataInputStream(socket.getInputStream())
//        val fileNameLength = dataInputStream.readInt()
//        println("Service is started")

        Test.test1()


        //return@launch

//        Database.connect("jdbc:sqlite:sample.db", driver = "org.sqlite.JDBC", user = "", password = "")

//        Database.connect("jdbc:sqlite:C:\\Users\\Dmitry\\Downloads\\better-adb-sync\\1.db", driver = "org.sqlite.JDBC", user = "", password = "")
//        transaction {
//            SchemaUtils.create(Tracks)
//            for (user in Tracks.selectAll()) {
//                println("${user[Tracks.rating]}: ${user[Tracks.name]}")
//            }
//        }

//        val mp3 = "C:\\Users\\Dmitry\\Music\\MusicBee\\Music\\5COPY\\My Little Medleys\\Canterlot Dreams (Piano).mp3".run {
//        val mp3 = "C:\\projects\\PlaylistRating\\1.mp3".run {
        val mp3 = "C:\\projects\\PlaylistRating\\1.opus".run {
            File(this)
        }
        val f = AudioFileIO.read(mp3)

        (f.tag as VorbisCommentTag).apply {
//        (f.tag as ID3v23Tag).apply {
            if (hasField(FieldKey.RATING)) {
                val x1 = getFields(FieldKey.RATING).first() as VorbisCommentTagField
                println(x1)
//                val x1 = getFields(FieldKey.RATING).first() as ID3v23Frame
//                deleteField(FieldKey.RATING)
//                x1.content = "MusicBee:64:0"
//                addField(FieldKey.RATING,   "64")
            }
        }

//        val newContent = first.content

//        AudioFileIO.write(f)
//        AudioFileIO.writeAs(f, "C:\\projects\\PlaylistRating\\2")

//        val opus = OggOpusStream.from(mp3.absolutePath)
//        val newTags = opus.commentHeader.tags.toMutableMap()
//            .put("RATING", listOf("30"))


//        val tags = opus.commentHeader.tags.toMutableMap()
//        opus.commentHeader.addTag("RATING", "30")
//
//
//
////        val binary: ByteArray = oggPage.dump()
////        opus.write(binary)
//        OggFileWriter.logger
        val x1 = File("C:\\projects\\PlaylistRating\\3.opus").readRatingFromAudioFile()


        println(x1)


    }
//    return AudioFileIO.read(this).run {
//        tag.getFirst(FieldKey.RATING)
//            .toIntOrNull()
//            ?.mapID3RatingCodeToSimpleRating()
//            ?: 0
//    }
}


@Composable
@Preview
fun App() {

    val screen = NavigationScreen()

    test1()
    MaterialTheme {
        screen.renderUI()
    }
}

//#EXT-X-RATING:5
fun main() = application {
    System.out.println("Hello World!");
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

