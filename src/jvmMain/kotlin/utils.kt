import org.chenliang.oggus.opus.OggOpusStream
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File

data class FailsInfoData(
    val path: String,
    val throwable: Throwable
)

fun String.findSongPath(): String? {
    val regex = "([^/])*/([^/])*/([^/])*([.])([^/])*".toRegex()
    return regex.find(this)?.groupValues?.firstOrNull()
}

fun createPlaylistWithRatings(
    organizedFolder: String,
    oldPlaylist: String,
    newPlaylist: String,
): ArrayList<FailsInfoData> {
//    val organizedFolder = "C:\\Users\\Dmitry\\Music\\MusicBee\\Music"
//    val playlist = "C:\\projects\\PlaylistRating\\1.m3u8".run {
//        File(this)
//    }

    var fails = ArrayList<FailsInfoData>()

    val playlist = File(oldPlaylist)

    val items = playlist.readLines()
    val map = items.associateBy({ it }, { "" }).toMutableMap()

    items
        .parallelStream()
        .forEach {
            val songInPc = organizedFolder + "//" + it.findSongPath()
            val rating = File(songInPc).run {
                try {
                    readRatingFromAudioFile()
                }catch (e: Throwable){
                    fails.add(
                        FailsInfoData(
                            songInPc, e
                        )
                    )
                    0
                }
            }
            map.put(it, "#EXT-X-RATING:$rating")
        }

    val sb = StringBuilder()
    map.map { (file, rating) ->
        sb.append(rating)
        sb.append("\n")
        sb.append(file)
        sb.append("\n")
    }

    val newFile = newPlaylist.run {
        File(this)
    }

    newFile.writer().apply {
        write(sb.toString())
        flush()
        close()
    }

    return fails
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