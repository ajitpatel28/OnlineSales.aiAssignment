package com.ajit.onlinesalesaiassignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ajit.onlinesalesaiassignment.ui.screens.HistoryTab
import com.ajit.onlinesalesaiassignment.ui.screens.MainScreen
import com.ajit.onlinesalesaiassignment.ui.screens.MainScreen
import com.ajit.onlinesalesaiassignment.ui.viewmodel.ExpressionViewModel

@Composable
fun MainNavGraph(
    navController: NavHostController,
    viewModel: ExpressionViewModel
) {
    NavHost(navController, startDestination = Destinations.MainScreen.route) {
        composable(Destinations.MainScreen.route) {
            MainScreen(viewModel, navController)
        }
        composable(Destinations.HistoryScreen.route) {
            HistoryTab(viewModel,navController)
        }
    }
}