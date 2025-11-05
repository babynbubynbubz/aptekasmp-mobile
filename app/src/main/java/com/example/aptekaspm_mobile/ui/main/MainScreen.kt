package com.example.aptekaspm_mobile.ui.main

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.aptekaspm_mobile.ui.navigation.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

import androidx.compose.material3.Scaffold

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cameraPermissionState.status.isGranted) {
                Box(modifier = Modifier.weight(1f)) {
                    CameraPreview(onBarcodeDetected = { viewModel.onBarcodeScanned(it) })
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                // Display for the scanned code info
                InfoDisplay(uiState)

                // Navigation buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val isNavEnabled = uiState.medicationInfo != null
                    Button(
                        onClick = {
                            uiState.medicationInfo?.info?.let { info ->
                                navController.navigate(
                                    Screen.Receive.createRoute(
                                        info.gid,
                                        info.sn,
                                        info.name,
                                        info.inn,
                                        info.inBoxAmount
                                    )
                                )
                            }
                        },
                        enabled = isNavEnabled
                    ) {
                        Text("Receive")
                    }
                    Button(
                        onClick = {
                            uiState.medicationInfo?.let { medInfo ->
                                medInfo.storageInfo?.let {
                                    navController.navigate(
                                        Screen.Restock.createRoute(
                                            gid = medInfo.info.gid,
                                            sn = medInfo.info.sn,
                                            name = medInfo.info.name,
                                            inn = medInfo.info.inn,
                                            inBoxAmount = medInfo.info.inBoxAmount,
                                            remainingAmount = it.inBoxRemaining,
                                            expiryDate = it.expiryDate
                                        )
                                    )
                                }
                            }
                        },
                        enabled = isNavEnabled && uiState.medicationInfo?.storageInfo != null
                    ) {
                        Text("Restock")
                    }
                    Button(onClick = { navController.navigate(Screen.Logs.route) }) {
                        Text("Logs")
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Camera permission is required to use the scanner.",
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                        Text("Grant Permission")
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoDisplay(uiState: MainScreenState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.error != null) {
            Text(text = uiState.error, color = MaterialTheme.colorScheme.error)
        }

        val info = uiState.medicationInfo
        if (info != null) {
            Text(
                text = "${info.info.name} (${info.info.inn})",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "GID: ${info.info.gid} / SN: ${info.info.sn}")
            Text(text = "Total in box: ${info.info.inBoxAmount}")
            info.storageInfo?.let {
                Text(text = "Remaining in box: ${it.inBoxRemaining}")
                Text(text = "Expires: ${it.expiryDate}")
            }
        } else if (uiState.scannedCode == null) {
            Text(text = "Scan something...")
        }
    }
}
