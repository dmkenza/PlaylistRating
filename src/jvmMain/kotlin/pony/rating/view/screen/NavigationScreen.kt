package pony.rating.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import pony.rating.view.screen.backsync.BackSyncScreen
import pony.rating.view.screen.playlist.PlaylistRatingsScreen

class NavigationScreen : Screen {

    val screens = listOf(
        "Playlist Rate" to PlaylistRatingsScreen(),
//        "Poweramp Sync" to BackSyncScreen()
    )

    @Composable
    override fun renderUI() {
        var tabIndex by remember { mutableStateOf(0) }
        val tabs = remember {
            screens.map { it.first }
        }

//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colors.primarySurface)
//            ) {
//                TabRow(
//                    modifier = Modifier.fillMaxWidth(0.4f),
//                    selectedTabIndex = tabIndex
//                ) {
//                    tabs.forEachIndexed { index, title ->
//                        Tab(text = { Text(title) },
//                            selected = tabIndex == index,
//                            onClick = { tabIndex = index }
//                        )
//                    }
//                }
//            }
//
//            screens.getOrNull(tabIndex)?.second?.renderUI()
//        }

        screens.getOrNull(tabIndex)?.second?.renderUI()
    }
}


