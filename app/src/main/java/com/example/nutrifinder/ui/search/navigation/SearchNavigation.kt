package com.example.nutrifinder.ui.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.nutrifinder.ui.search.SearchRoute

const val searchNavigationRoute = "search_route"

fun NavController.navigateToSearch(builder: (NavOptionsBuilder.() -> Unit)? = null) {
    this.navigate(searchNavigationRoute, builder ?: {})
}

fun NavGraphBuilder.search() {
    composable(
        route = searchNavigationRoute
    ) {
        SearchRoute()
    }
}