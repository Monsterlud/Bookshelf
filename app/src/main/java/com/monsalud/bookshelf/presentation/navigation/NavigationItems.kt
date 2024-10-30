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
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.monsalud.bookshelf.R


@Composable
fun getNavigationItems(): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = stringResource(id = R.string.list_title_fiction),
            listName = stringResource(id = R.string.list_name_fiction),
            selectedIcon = Icons.Filled.Book,
            unselectedIcon = Icons.Outlined.Book,
        ),
        NavigationItem(
            title = stringResource(id = R.string.list_title_nonfiction),
            listName = stringResource(id = R.string.list_name_nonfiction),
            selectedIcon = Icons.Filled.Balance,
            unselectedIcon = Icons.Outlined.Balance,
        ),
        NavigationItem(
            title = stringResource(id = R.string.list_title_culture),
            listName = stringResource(id = R.string.list_name_culture),
            selectedIcon = Icons.Filled.Museum,
            unselectedIcon = Icons.Outlined.Museum,
        ),
        NavigationItem(
            title = stringResource(id = R.string.list_title_food_and_diet),
            listName = stringResource(id = R.string.list_name_food_and_diet),
            selectedIcon = Icons.Filled.Fastfood,
            unselectedIcon = Icons.Outlined.Fastfood,
        ),
        NavigationItem(
            title = stringResource(id = R.string.list_title_graphic_books),
            listName = stringResource(id = R.string.list_name_graphic_books),
            selectedIcon = Icons.Filled.Draw,
            unselectedIcon = Icons.Outlined.Draw,
        ),
        NavigationItem(
            title = stringResource(id = R.string.list_title_politics),
            listName = stringResource(id = R.string.list_name_politics),
            selectedIcon = Icons.Filled.HistoryEdu,
            unselectedIcon = Icons.Outlined.HistoryEdu,
        ),
        NavigationItem(
            title = stringResource(id = R.string.list_title_science),
            listName = stringResource(id = R.string.list_name_science),
            selectedIcon = Icons.Filled.Science,
            unselectedIcon = Icons.Outlined.Science,
        ),
        NavigationItem(
            title = stringResource(id = R.string.list_title_sports),
            listName = stringResource(id = R.string.list_name_sports),
            selectedIcon = Icons.Filled.SportsFootball,
            unselectedIcon = Icons.Outlined.SportsFootball,
        ),
        NavigationItem(
            title = stringResource(id = R.string.list_title_travel),
            listName = stringResource(id = R.string.list_name_travel),
            selectedIcon = Icons.Filled.TravelExplore,
            unselectedIcon = Icons.Outlined.TravelExplore,
        ),
    )
}
