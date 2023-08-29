package com.ajit.onlinesalesaiassignment.ui.navigation


sealed class Destinations(val route: String) {
    object MainScreen : Destinations("main_screen")
    object HistoryScreen : Destinations("history_screen")
}
