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
import javax.inject.Inject

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

data class ReceiveScreenState(
    val isLoading: Boolean = false,
    val gid: String = "",
    val sn: String = "",
    val name: String = "",
    val inn: String = "",
    val inBoxAmount: Int = 0,
    val expiryDate: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false
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
                gid = savedStateHandle.get<String>("gid") ?: "",
                sn = savedStateHandle.get<String>("sn") ?: "",
                name = URLDecoder.decode(savedStateHandle.get<String>("name") ?: "", StandardCharsets.UTF_8.toString()),
                inn = URLDecoder.decode(savedStateHandle.get<String>("inn") ?: "", StandardCharsets.UTF_8.toString()),
                inBoxAmount = savedStateHandle.get<Int>("inBoxAmount") ?: 0
            )
        }
    }

    fun onExpiryDateChanged(date: String) {
        _uiState.update { it.copy(expiryDate = date) }
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
                    gid = _uiState.value.gid,
                    sn = _uiState.value.sn,
                    expiryDate = _uiState.value.expiryDate
                )
                // val response = apiService.receiveMedication(request)
                // if (response.isSuccessful) {
                //    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                // } else {
                //    _uiState.update { it.copy(isLoading = false, error = "Receive failed") }
                // }

                // Simulate API call
                kotlinx.coroutines.delay(1000)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
