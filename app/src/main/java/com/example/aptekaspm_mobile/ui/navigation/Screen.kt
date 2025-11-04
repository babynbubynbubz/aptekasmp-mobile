package com.example.aptekaspm_mobile.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Receive : Screen("receive_screen")
    object Restock : Screen("restock_screen")
    object Logs : Screen("logs_screen")
}