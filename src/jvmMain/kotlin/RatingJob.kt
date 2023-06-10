import kotlinx.coroutines.*
import java.lang.Exception

var currentJob: Job? = null

fun createRatingJob(
    scope: CoroutineScope,
    config: ConfigData,
    onStateChanged: (String) -> Unit,
) {
    val (organizedFolder, oldPlaylist, newPlaylist) = config
    currentJob?.cancel()
    currentJob = scope.async(Dispatchers.IO) {
        try {
            onStateChanged("started")
            val fails = createPlaylistWithRatings(organizedFolder, oldPlaylist, newPlaylist)
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