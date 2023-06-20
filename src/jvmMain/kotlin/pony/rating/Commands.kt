package pony.rating

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import java.io.BufferedReader
import java.io.InputStreamReader


data class CommandsGenerator(
    val workingFolder: String,
) {
    fun adbListVolumes(): Command = Command(
        workingFolder = workingFolder,
        exeFile = "adb.exe",
        args = "shell sm list-volumes public"
    )

    fun adbSyncTransfer(
        type: TransferType,
        sourceFile: String,
        destinationFile: String,
    ) = Command(
        workingFolder = workingFolder,
        exeFile = "adbsync.exe",
        args = "${type.value} $sourceFile $destinationFile"
    )

    enum class TransferType(val value: String) {
        PUSH("push"), PULL("pull")
    }
}

object CommandExecutor {

    suspend fun execute(cmd: Command): CommandResult {
        return try {
            executeCommand(cmd)
        } catch (e: Throwable) {
            CommandResult(
                error = e
            )
        }
    }

    suspend fun executeCommand(cmd: Command) = withContext(Dispatchers.IO) {
        var (workingFolder, exeFile, args) = cmd
//        val workingFolder = "C:\\projects\\PlaylistRating\\adb\\"
        val sb = StringBuilder()

        args = args.replace("  ", "")
        val process = ProcessBuilder(
            workingFolder + exeFile,
            * args.split(" ").map {
                it.replace("%20", " ")
            }.toTypedArray()
        ).apply {
            redirectErrorStream(true)
        }.start().apply {
            val out = BufferedReader(InputStreamReader(getInputStream()))
            var line: String?
            while (out.readLine().also { line = it } != null) {
                sb.append(line + "\n")
                println(line)
               delay(10)
            }
            waitFor()
        }
        val log = sb.toString()

        CommandResult(
            out = log,
            exitCode = process.exitValue()
        )
    }

}


data class Command(
    val workingFolder: String,
    val exeFile: String,
    val args: String,
)

data class CommandResult(
    val exitCode: Int = -1,
    val out: String = "",
    val error: Throwable? = null,
)

