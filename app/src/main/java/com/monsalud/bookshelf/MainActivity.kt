package com.monsalud.bookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.monsalud.bookshelf.presentation.components.BookshelfTopBar
import com.monsalud.bookshelf.presentation.navigation.BookshelfNavHost
import com.monsalud.bookshelf.presentation.navigation.NavDrawer
import com.monsalud.bookshelf.ui.theme.BookshelfTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BookshelfTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                Surface(tonalElevation = 5.dp) {
                    NavDrawer(
                        drawerState = drawerState,
                        navController = navController,
                    ) {
                        Scaffold(
                            topBar = {
                                BookshelfTopBar(
                                    navController = navController,
                                    onMenuClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    },
                                    onBackClick = {
                                        navController.popBackStack()
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            BookshelfNavHost(
                                navController = navController,
                                innerPadding = it
                            )
                        }
                    }
                }
            }
        }
    }
}
