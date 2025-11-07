package com.example.aptekaspm_mobile.ui.receive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiveScreen(
    navController: NavController,
    viewModel: ReceiveViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.previousBackStackEntry?.savedStateHandle?.set("should_clear", true)
            navController.popBackStack()
        }
    }

    if (uiState.showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = viewModel::onDismissDatePicker,
            confirmButton = {
                TextButton(onClick = { datePickerState.selectedDateMillis?.let(viewModel::onExpiryDateSelected) }) {
                    Text("ОК")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onDismissDatePicker) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
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
            Text("Приемка лекарства", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Text("${uiState.name} (${uiState.inn})", style = MaterialTheme.typography.titleMedium)
            Text("GID: ${uiState.gid} / SN: ${uiState.sn}")
            Text("Всего в упаковке: ${uiState.inBoxAmount}")
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = viewModel::onShowDatePicker) {
                Text(text = if (uiState.expiryDate.isNotBlank()) uiState.expiryDate else "Выберите срок годности")
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = viewModel::receiveMedication,
                    enabled = uiState.expiryDate.isNotBlank()
                ) {
                    Text("Подтвердить приемку")
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
                Text("Назад")
            }
        }
    }
}