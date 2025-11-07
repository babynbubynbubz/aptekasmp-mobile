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

sealed class NavigationEvent {
    data class ToReceive(
        val scanData: String,
        val info: MedicationInfoResponse,
    ) : NavigationEvent()

    data class ToDispense(
        val scanData: String,
        val medInfo: MedicationInfoResponse,
    ) : NavigationEvent()
}

data class MainScreenState(
    val isLoading: Boolean = false,
    val scannedCode: String? = null,
    val medicationInfo: MedicationInfoResponse? = null,
    val error: String? = null,
    val navigationEvent: NavigationEvent? = null,
    val seriesMedkitId: String? = null,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    fun onBarcodeScanned(barcode: String) {
        if (barcode == _uiState.value.scannedCode && _uiState.value.navigationEvent == null) return

        _uiState.update {
            it.copy(
                isLoading = true,
                scannedCode = barcode,
                medicationInfo = null,
                error = null,
                navigationEvent = null
            )
        }

        viewModelScope.launch {
            try {
                val request = MedicationInfoRequest(scanData = barcode)
                val response = apiService.getMedicationInfo(request)

                if (response.isSuccessful) {
                    val medicationInfo = response.body()
                    if (medicationInfo != null) {
                        val navigationEvent = if (medicationInfo.storageInfo == null) {
                            NavigationEvent.ToReceive(barcode, medicationInfo)
                        } else {
                            NavigationEvent.ToDispense(barcode, medicationInfo)
                        }
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                medicationInfo = medicationInfo,
                                navigationEvent = navigationEvent
                            )
                        }
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

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = null) }
    }

    fun clearScanData() {
        _uiState.update {
            it.copy(
                scannedCode = null,
                medicationInfo = null,
                error = null,
                navigationEvent = null
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun setSeriesMedkitId(medkitId: String?) {
        _uiState.update { it.copy(seriesMedkitId = medkitId) }
    }
}
