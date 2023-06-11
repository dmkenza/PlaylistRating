package pony.rating.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import pony.rating.CommandsGenerator
import pony.rating.data.FileDeviceReaderRepository
import java.io.File

val scope = CoroutineScope(SupervisorJob())

val commandsGenerator = CommandsGenerator(
    File("adb").absolutePath + "\\"
)