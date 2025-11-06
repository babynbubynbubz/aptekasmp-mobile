package com.example.aptekaspm_mobile.ui.dispense

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aptekaspm_mobile.data.network.ApiService
import com.example.aptekaspm_mobile.data.network.models.DispenseRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

data class DispenseScreenState(
    val isLoading: Boolean = false,
    val scanData: String = "",
    val gid: String = "",
    val sn: String = "",
    val name: String = "",
    val inn: String = "",
    val inBoxAmount: Int = 0,
    val remainingAmount: Int = 0,
    val expiryDate: String = "",
    val medkitId: String = "",
    val transferAmount: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class DispenseViewModel @Inject constructor(
    private val apiService: ApiService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DispenseScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                scanData = URLDecoder.decode(savedStateHandle.get<String>("scanData") ?: "", StandardCharsets.UTF_8.toString()),
                gid = savedStateHandle.get<String>("gid") ?: "",
                sn = savedStateHandle.get<String>("sn") ?: "",
                name = URLDecoder.decode(
                    savedStateHandle.get<String>("name") ?: "",
                    StandardCharsets.UTF_8.toString()
                ),
                inn = URLDecoder.decode(
                    savedStateHandle.get<String>("inn") ?: "",
                    StandardCharsets.UTF_8.toString()
                ),
                inBoxAmount = savedStateHandle.get<Int>("inBoxAmount") ?: 0,
                remainingAmount = savedStateHandle.get<Int>("remainingAmount") ?: 0,
                expiryDate = URLDecoder.decode(
                    savedStateHandle.get<String>("expiryDate") ?: "",
                    StandardCharsets.UTF_8.toString()
                )
            )
        }
    }

    fun onMedkitIdChanged(id: String) {
        _uiState.update { it.copy(medkitId = id) }
    }

    fun onTransferAmountChanged(amount: String) {
        _uiState.update { it.copy(transferAmount = amount) }
    }

    fun dispenseMedication() {
        val medkitId = _uiState.value.medkitId.toIntOrNull()
        val transferAmount = _uiState.value.transferAmount.toIntOrNull()

        if (medkitId == null || transferAmount == null) {
            _uiState.update { it.copy(error = "Invalid Medkit ID or Amount") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val request = DispenseRequest(
                    scanData = _uiState.value.scanData,
                    medkitId = medkitId,
                    transferAmount = transferAmount
                )
                val response = apiService.dispenseMedication(request)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Dispense failed: ${response.message()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
