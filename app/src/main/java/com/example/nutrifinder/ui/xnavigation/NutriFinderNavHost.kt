package com.example.nutrifinder.ui.xnavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.nutrifinder.ui.search.navigation.search
import com.example.nutrifinder.ui.search.navigation.searchNavigationRoute

@Composable
fun NutriFinderNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = searchNavigationRoute
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        search()
    }
}