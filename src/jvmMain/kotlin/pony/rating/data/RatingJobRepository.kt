package pony.rating.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import pony.rating.ConfigData
import pony.rating.di.scope
import pony.rating.isPhonePath
import pony.rating.tools.findSongPath
import pony.rating.tools.readRatingFromAudioFile
import java.io.File
import java.lang.Exception

class RatingJobRepository {

    var currentJob: Job? = null
    val outputLogFlow = MutableStateFlow<String>("")

    val fileDeviceReaderRepository = FileDeviceReaderRepository()

    fun start(
        config: ConfigData,
    ) {
        var (organizedFolder, oldPlaylist, newPlaylist) = config
        currentJob?.cancel()
        currentJob = scope.async(Dispatchers.IO) {
            try {
                onStateChanged("started")

                if (oldPlaylist.isPhonePath()){
                    oldPlaylist = fileDeviceReaderRepository.readDeviceFileToTempOnPc(oldPlaylist).absolutePath
                }

                val (text, fails) = readPlaylistWithRatings(organizedFolder, oldPlaylist)

                if (newPlaylist.isPhonePath()){
                    savePlaylistToPc(text, oldPlaylist)
                    fileDeviceReaderRepository.savePcFileOntoDevice(
                        oldPlaylist, newPlaylist
                    )
                }else{
                    savePlaylistToPc(text, newPlaylist)
                }

                if (fails.size > 0) {
                    val sb = StringBuilder()
                    fails.map { (path, err) ->
                        sb.append(path + "\n" + err.message + "\n\n")
                    }
                    onStateChanged("finished with ${fails.size} fails\n" + sb.toString())

                } else {
                    onStateChanged("finished successfully")
                }
            } catch (e: Exception) {
                onStateChanged(e.toString())
            }

        }
    }


    private fun onStateChanged(msg: String){
        outputLogFlow.value = msg
    }


    fun readPlayList(){

    }

    fun savePlaylistToPc(
        text: String,
        newPlaylist: String
    ){
        val newFile = newPlaylist.run {
            File(this)
        }

        newFile.writer().apply {
            write(text)
            flush()
            close()
        }
    }

    fun readPlaylistWithRatings(
        organizedFolder: String,
        oldPlaylist: String,
    ): Pair<String, ArrayList<FailsInfoData>> {

        var fails = ArrayList<FailsInfoData>()

        val playlist = File(oldPlaylist)

        val items = playlist.readLines().filter {
            !it.startsWith("#")
        }
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

        val text = sb.toString()

        return text to fails
    }

    data class FailsInfoData(
        val path: String,
        val throwable: Throwable
    )

}
