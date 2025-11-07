package com.example.aptekaspm_mobile.ui.logs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.aptekaspm_mobile.data.network.models.DispensingLogItem
import com.example.aptekaspm_mobile.data.network.models.ReceivingLogItem
import com.example.aptekaspm_mobile.ui.utils.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    navController: NavController,
    viewModel: LogsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val tabs = listOf("Dispensing Logs", "Receiving Logs")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Logs") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            TabRow(
                modifier = Modifier.navigationBarsPadding(),
                selectedTabIndex = uiState.selectedTab
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = uiState.selectedTab == index,
                        onClick = { viewModel.onTabSelected(index) },
                        text = { Text(title) }
                    )
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
                }
            } else {
                when (uiState.selectedTab) {
                    0 -> DispensingLogsList(uiState.dispensingLogs)
                    1 -> ReceivingLogsList(uiState.receivingLogs)
                }
            }
        }
    }
}

@Composable
fun DispensingLogsList(logs: List<DispensingLogItem>) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(logs) { log ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Medication: ${log.medicationName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text("GID: ${log.gid}")
                    Text("SN: ${log.sn}")
                    Text("To Medkit: ${log.medkitId}")
                    Text("Amount: ${log.transferAmount}")
                    Text("Date: ${formatDate(log.transferDate)}")
                }
            }
        }
    }
}

@Composable
fun ReceivingLogsList(logs: List<ReceivingLogItem>) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(logs) { log ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Medication: ${log.medicationName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text("GID: ${log.gid}")
                    Text("SN: ${log.sn}")
                    Text("Received: ${formatDate(log.receiveDate)}")
                    Text("Expires: ${formatDate(log.expiryDate)}")
                }
            }
        }
    }
}