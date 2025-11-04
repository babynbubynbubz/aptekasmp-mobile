package com.example.aptekaspm_mobile.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aptekaspm_mobile.ui.navigation.Screen

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(Screen.Receive.route) }) {
            Text("Receive Medicine")
        }
        Button(onClick = { navController.navigate(Screen.Restock.route) }) {
            Text("Restock Medkit")
        }
        Button(onClick = { navController.navigate(Screen.Logs.route) }) {
            Text("View Logs")
        }
    }
}
