package com.example.aptekaspm_mobile.ui.receive

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ReceiveScreen(
    navController: NavController,
    viewModel: ReceiveViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Receive Medication", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text("${uiState.name} (${uiState.inn})", style = MaterialTheme.typography.titleMedium)
        Text("GID: ${uiState.gid} / SN: ${uiState.sn}")
        Text("Amount in box: ${uiState.inBoxAmount}")
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.expiryDate,
            onValueChange = viewModel::onExpiryDateChanged,
            label = { Text("Expiry Date (YYYY-MM-DD)") },
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = viewModel::receiveMedication) {
                Text("Confirm Receive")
            }
        }

        uiState.error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}