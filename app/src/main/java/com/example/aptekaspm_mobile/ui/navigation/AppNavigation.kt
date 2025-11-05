package com.example.aptekaspm_mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aptekaspm_mobile.ui.logs.LogsScreen
import com.example.aptekaspm_mobile.ui.main.MainScreen
import com.example.aptekaspm_mobile.ui.receive.ReceiveScreen
import com.example.aptekaspm_mobile.ui.restock.RestockScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(navController = navController)
        }
        composable(
            route = Screen.Receive.route,
            arguments = listOf(
                navArgument("gid") { type = NavType.StringType },
                navArgument("sn") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("inn") { type = NavType.StringType },
                navArgument("inBoxAmount") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            ReceiveScreen(navController = navController)
        }
        composable(
            route = Screen.Restock.route,
            arguments = listOf(
                navArgument("gid") { type = NavType.StringType },
                navArgument("sn") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("inn") { type = NavType.StringType },
                navArgument("inBoxAmount") { type = NavType.IntType },
                navArgument("remainingAmount") { type = NavType.IntType },
                navArgument("expiryDate") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            RestockScreen(navController = navController)
        }
        composable(Screen.Logs.route) {
            LogsScreen(navController = navController)
        }
    }
}
