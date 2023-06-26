package pony.rating.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import pony.rating.CommandExecutor
import pony.rating.di.commandsGenerator
import pony.rating.di.scope

class PhonePathsRepository() {


    val phonePathsFlow = MutableStateFlow(PhonePaths())

    init {
        scope.launch {
            while (true) {
                try {
//                    return@launch
                    start()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
                delay(5000)
            }
        }
    }

    private suspend fun start() {
        commandsGenerator.adbListVolumes().apply {
            val res = CommandExecutor.execute(this)
            if (res.exitCode != 0) {
                val errorStack = res.error?.stackTraceToString() + ""
                PhonePaths().run(
                    phonePathsFlow::tryEmit
                )
                println("Error with ${res.exitCode} ${res.out} $errorStack")
            } else {
                //println("Success ${res.out}")
                val list = parseResult(res.out)
                PhonePaths(list).run(
                    phonePathsFlow::tryEmit
                )
            }
        }
    }

    //public:179,1 mounted 417A-1C12,2 mounted 417A-1C13
    fun parseResult(str: String): List<PhonePathLink> {
        return str.split(",").mapNotNull {
            when {
                it.contains("public:") -> {
                    PhonePathLink(
                        "internal sdcard", "/storage/self/primary/"
                    )
                }
                ///storage/417A-1C12
                it.contains("mounted") -> {
                    val cardName = it.split(" ").lastOrNull()?.trim() ?: return@mapNotNull null
                    PhonePathLink(
                        cardName, "/storage/$cardName/"
                    )
                }
                else -> {
                    null
                }
            }
        }
    }

}

data class PhonePaths(
    val paths: List<PhonePathLink> = emptyList(),
)

data class PhonePathLink(
    val pseudo: String = "",
    val fullPath: String = "",
)