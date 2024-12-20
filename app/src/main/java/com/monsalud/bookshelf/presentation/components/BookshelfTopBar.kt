package com.monsalud.bookshelf.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.monsalud.bookshelf.R
import com.monsalud.bookshelf.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfTopBar(
    navController: NavController,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isDetailScreen = currentBackStackEntry?.destination?.hasRoute<Screen.BookDetailScreen>()

    TopAppBar(
        title = {
            Text(
                text =
                if (isDetailScreen == true) stringResource(id = R.string.top_bar_title_detail)
                else stringResource(id = R.string.top_bar_title_list)
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (isDetailScreen == true) {
                    onBackClick()
                } else {
                    onMenuClick()
                }
            }) {
                Icon(
                    imageVector =
                    if (isDetailScreen == true) {
                        Icons.AutoMirrored.Filled.ArrowBack
                    } else {
                        Icons.Default.Menu
                    },
                    contentDescription =
                    if (isDetailScreen == true) stringResource(id = R.string.back_arrow)
                    else stringResource(id = R.string.navigation_menu)
                )
            }
        },
        modifier = modifier,
    )
}
