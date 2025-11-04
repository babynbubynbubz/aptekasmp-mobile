package com.example.aptekaspm_mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aptekaspm_mobile.ui.logs.LogsScreen
import com.example.aptekaspm_mobile.ui.main.MainScreen
import com.example.aptekaspm_mobile.ui.receive.ReceiveScreen
import com.example.aptekaspm_mobile.ui.restock.RestockScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen()
        }
        composable(Screen.Receive.route) {
            ReceiveScreen()
        }
        composable(Screen.Restock.route) {
            RestockScreen()
        }
        composable(Screen.Logs.route) {
            LogsScreen()
        }
    }
}
