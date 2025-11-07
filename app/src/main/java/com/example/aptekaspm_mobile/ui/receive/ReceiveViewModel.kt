package com.example.aptekaspm_mobile.ui.receive

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aptekaspm_mobile.data.network.ApiService
import com.example.aptekaspm_mobile.data.network.models.ReceiveRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class ReceiveScreenState(
    val isLoading: Boolean = false,
    val scanData: String = "",
    val gid: String = "",
    val sn: String = "",
    val name: String = "",
    val inn: String = "",
    val inBoxAmount: Int = 0,
    val expiryDate: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false,
    val showDatePickerDialog: Boolean = false
)

@HiltViewModel
class ReceiveViewModel @Inject constructor(
    private val apiService: ApiService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiveScreenState())
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
                inBoxAmount = savedStateHandle.get<Int>("inBoxAmount") ?: 0
            )
        }
    }

    fun onShowDatePicker() {
        _uiState.update { it.copy(showDatePickerDialog = true) }
    }

    fun onDismissDatePicker() {
        _uiState.update { it.copy(showDatePickerDialog = false) }
    }

    fun onExpiryDateSelected(dateMillis: Long) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = sdf.format(Date(dateMillis))
        _uiState.update { it.copy(expiryDate = formattedDate, showDatePickerDialog = false) }
    }

    fun receiveMedication() {
        if (_uiState.value.expiryDate.isBlank()) {
            _uiState.update { it.copy(error = "Expiry Date cannot be empty") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val request = ReceiveRequest(
                    scanData = _uiState.value.scanData,
                    expiryDate = _uiState.value.expiryDate
                )
                val response = apiService.receiveMedication(request)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Receive failed: ${response.message()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
