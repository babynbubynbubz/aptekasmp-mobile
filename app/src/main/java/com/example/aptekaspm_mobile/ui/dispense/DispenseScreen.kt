package com.example.aptekaspm_mobile.ui.dispense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.aptekaspm_mobile.ui.utils.formatDate

@Composable
fun DispenseScreen(
    navController: NavController,
    viewModel: DispenseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.previousBackStackEntry?.savedStateHandle?.set("should_clear", true)
            if (uiState.seriesStarted) {
                navController.previousBackStackEntry?.savedStateHandle?.set("newSeriesMedkitId", uiState.medkitId)
            }
            navController.popBackStack()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Dispense Medication", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Text("${uiState.name} (${uiState.inn})", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Expires: ${formatDate(uiState.expiryDate)}",
                color = if (uiState.isExpired) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            Text("Amount: ${uiState.remainingAmount}/${uiState.inBoxAmount}")

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.medkitId,
                onValueChange = viewModel::onMedkitIdChanged,
                label = { Text("Medkit ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.transferAmount,
                onValueChange = viewModel::onTransferAmountChanged,
                label = { Text("Transfer Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = viewModel::dispenseMedication,
                        enabled = uiState.medkitId.isNotBlank() && uiState.transferAmount.isNotBlank() && !uiState.isExpired
                    ) {
                        Text("Dispense")
                    }
                    Button(
                        onClick = viewModel::dispenseAndStartSeries,
                        enabled = uiState.medkitId.isNotBlank() && uiState.transferAmount.isNotBlank() && !uiState.isExpired
                    ) {
                        Text("Dispense and Start Series")
                    }
                }
            }

            uiState.error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { 
                navController.previousBackStackEntry?.savedStateHandle?.set("should_clear", true)
                navController.popBackStack() 
            }) {
                Text("Back")
            }
        }
    }
}