package pony.rating.data

import pony.rating.CommandExecutor
import pony.rating.CommandsGenerator
import pony.rating.di.commandsGenerator
import pony.rating.di.scope
import java.io.File

class FileDeviceReaderRepository {

    suspend fun savePcFileOntoDevice(
        source: String,
        destination: String
    ) {
        val cmd = commandsGenerator.adbSyncTransfer(
            CommandsGenerator.TransferType.PUSH,
            source,
            destination
        )

        val res = CommandExecutor.execute(cmd)

        if (res.exitCode != 0) {
            val errorStack = res.error?.stackTraceToString() + ""
            println("Error with ${res.exitCode} ${res.out} $errorStack")
            if (res.error != null) {
                throw res.error
            } else {
                throw Exception("Error with ${res.exitCode} ${res.out}")
            }
        }
    }


    suspend fun readDeviceFileToTempOnPc(path: String): File {
        val temp = File.createTempFile("playlist_", ".m3u8").apply {
            delete()
        }.absolutePath


//        val temp = "C:\\Users\\Dmitry\\Downloads\\better-adb-sync\\src\\2.txt"

        val cmd = commandsGenerator.adbSyncTransfer(
            CommandsGenerator.TransferType.PULL,
            path,
            temp
        )

        val res = CommandExecutor.execute(cmd)

        if (res.exitCode != 0) {
            val errorStack = res.error?.stackTraceToString() + ""
            println("Error with ${res.exitCode} ${res.out} $errorStack")
            if (res.error != null) {
                throw res.error
            } else {
                throw Exception("Error with ${res.exitCode} ${res.out}")
            }
        } else {
            return File(temp)
        }
    }
}