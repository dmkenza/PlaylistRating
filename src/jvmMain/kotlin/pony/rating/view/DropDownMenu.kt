package pony.rating.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import pony.rating.data.PhonePathLink
import pony.rating.data.PhonePaths

@Composable
fun PathsDropdownMenu(
    phonePaths: PhonePaths,
    onClick: ((item: PhonePathLink) -> Unit)? = null,
) {

    val paths = phonePaths.paths
    var expanded by remember { mutableStateOf(false) }
    Row(modifier = Modifier
        .clickable {
            expanded = true
        }
    ) {
        Text("sdcard")
        Box {
            Image(
                painter = rememberVectorPainter(Icons.Rounded.ArrowDropDown),
                contentDescription = "",
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                paths.forEach { path ->
                    DropdownMenuItem(onClick = {
                        onClick?.invoke(path)
                        expanded = false
                    }) {
                        Text(path.pseudo)
                    }
                }
            }
        }
    }
}