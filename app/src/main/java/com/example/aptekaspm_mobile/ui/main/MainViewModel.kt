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
                medicationInfo = null,
                error = null
            )
        }

        viewModelScope.launch {
            try {
                val request = MedicationInfoRequest(scanData = barcode)
                val response = apiService.getMedicationInfo(request)

                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            medicationInfo = response.body()
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error: ${response.code()} ${response.message()}"
                        )
                    }
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
