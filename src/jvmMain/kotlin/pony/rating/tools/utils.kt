package pony.rating.tools

import dorkbox.notify.Notify
import dorkbox.notify.Pos
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.chenliang.oggus.opus.OggOpusStream
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File


fun String.findSongPath(): String? {
    val regex = "([^/])*/([^/])*/([^/])*([.])([^/])*".toRegex()
    return regex.find(this)?.groupValues?.firstOrNull()
}


private fun File.readRatingMp3(): Int {
    return AudioFileIO.read(this).run {
        tag.getFirst(FieldKey.RATING)
            .toIntOrNull()
            ?.mapID3RatingCodeToSimpleRating()
            ?: 0
    }
}

private fun File.readRatingOgg(): Int {
    val inputStream = inputStream()
    val opus = OggOpusStream.from(inputStream)
    return opus.run {
        val rating = commentHeader.tags
            .get("RATING")
            ?.stream()
            ?.toList()
            ?.firstOrNull()
            ?.toInt()?.run {
                this / 20
            } ?: 0
        runCatching {
            inputStream.close()
        }
        rating
    }
}

fun File.readRatingFromAudioFile(): Int = when (this.extension) {
    "ogg", "opus" -> readRatingOgg()
    "mp3" -> readRatingMp3()
    else -> 0
}


fun Int.mapID3RatingCodeToSimpleRating() = when (this) {
    1 -> 1
    64 -> 2
    128 -> 3
    196 -> 4
    255 -> 5
    else -> 0
}


fun debug(msg: String) = System.out.println(msg)

var notify = Notify.create()

@OptIn(DelicateCoroutinesApi::class)
fun toast(msg: String) {

    GlobalScope.launch(Dispatchers.IO) {
        println(msg)
        notify.title("PlaylistRatings")
            .text(msg)
            .position(Pos.TOP_RIGHT)
            .hideAfter(2000)
            .shake(250, 10)
//            .darkStyle() // There are two default themes darkStyle() and default.
            .showInformation()
    }

}