package com.example.aptekaspm_mobile.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aptekaspm_mobile.data.network.ApiService
import com.example.aptekaspm_mobile.data.network.models.MedicationInfoRequest
import com.example.aptekaspm_mobile.data.network.models.MedicationInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainScreenState(
    val isLoading: Boolean = false,
    val scannedCode: String? = null,
    val medicationInfo: MedicationInfoResponse? = null,
    val error: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    fun onBarcodeScanned(barcode: String) {
        // Prevent re-scanning the same code
        if (barcode == _uiState.value.scannedCode) return

        _uiState.update {
            it.copy(
                isLoading = true,
                scannedCode = barcode,
                medicationInfo = null, // Clear previous info
                error = null
            )
        }

        viewModelScope.launch {
            try {
                // In a real app, you would make the network call here.
                // For now, we'll simulate a successful response with dummy data
                // as the API is not live.
                val request = MedicationInfoRequest(scanData = barcode)
                // val response = apiService.getMedicationInfo(request)

                // FAKE RESPONSE FOR DEVELOPMENT
                val fakeResponse = MedicationInfoResponse(
                    info = com.example.aptekaspm_mobile.data.network.models.MedicationDetails(
                        name = "Super Med",
                        inn = "Medicus Supericus",
                        inBoxAmount = 100,
                        gid = "GID12345",
                        sn = "SN67890"
                    ),
                    storageInfo = com.example.aptekaspm_mobile.data.network.models.StorageInfo(
                        inBoxRemaining = 50,
                        expiryDate = "2025-12-31"
                    )
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        medicationInfo = fakeResponse
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to get medication info: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
