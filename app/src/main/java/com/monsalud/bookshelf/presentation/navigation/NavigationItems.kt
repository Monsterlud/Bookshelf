package com.monsalud.bookshelf.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.Museum
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.SportsFootball
import androidx.compose.material.icons.outlined.TravelExplore

val navigationItems = listOf(
    NavigationItem(
        title = "Fiction",
        listName = "Hardcover Fiction",
        selectedIcon = Icons.Filled.Book,
        unselectedIcon = Icons.Outlined.Book,
    ),
    NavigationItem(
        title = "Non-Fiction",
        listName = "Hardcover Nonfiction",
        selectedIcon = Icons.Filled.Balance,
        unselectedIcon = Icons.Outlined.Balance,
    ),
    NavigationItem(
        title = "Culture",
        listName = "Culture",
        selectedIcon = Icons.Filled.Museum,
        unselectedIcon = Icons.Outlined.Museum,
    ),
    NavigationItem(
        title = "Food and Diet",
        listName = "Food and Fitness",
        selectedIcon = Icons.Filled.Fastfood,
        unselectedIcon = Icons.Outlined.Fastfood,
    ),
    NavigationItem(
        title = "Graphic Books and Manga",
        listName = "Graphic Books and Manga",
        selectedIcon = Icons.Filled.Draw,
        unselectedIcon = Icons.Outlined.Draw,
    ),
    NavigationItem(
        title = "Politics and American History",
        listName = "Hardcover Political Books",
        selectedIcon = Icons.Filled.HistoryEdu,
        unselectedIcon = Icons.Outlined.HistoryEdu,
    ),
    NavigationItem(
        title = "Science",
        listName = "Science",
        selectedIcon = Icons.Filled.Science,
        unselectedIcon = Icons.Outlined.Science,
    ),
    NavigationItem(
        title = "Sports and Fitness",
        listName = "Sports",
        selectedIcon = Icons.Filled.SportsFootball,
        unselectedIcon = Icons.Outlined.SportsFootball,
    ),
    NavigationItem(
        title = "Travel",
        listName = "Travel",
        selectedIcon = Icons.Filled.TravelExplore,
        unselectedIcon = Icons.Outlined.TravelExplore,
    ),
)
