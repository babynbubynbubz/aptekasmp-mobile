package com.example.aptekaspm_mobile.ui.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aptekaspm_mobile.data.network.ApiService
import com.example.aptekaspm_mobile.data.network.models.DispensingLogItem
import com.example.aptekaspm_mobile.data.network.models.ReceivingLogItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogsScreenState(
    val isLoading: Boolean = false,
    val dispensingLogs: List<DispensingLogItem> = emptyList(),
    val receivingLogs: List<ReceivingLogItem> = emptyList(),
    val error: String? = null,
    val selectedTab: Int = 0
)

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogsScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchLogs()
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun fetchLogs() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val dispensingLogsDeferred = async { apiService.getDispensingLogs() }
                val receivingLogsDeferred = async { apiService.getReceivingLogs() }

                val dispensingLogsResponse = dispensingLogsDeferred.await()
                val receivingLogsResponse = receivingLogsDeferred.await()

                if (dispensingLogsResponse.isSuccessful && receivingLogsResponse.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            dispensingLogs = dispensingLogsResponse.body() ?: emptyList(),
                            receivingLogs = receivingLogsResponse.body() ?: emptyList()
                        )
                    }
                } else {
                    val error =
                        "Dispensing logs error: ${dispensingLogsResponse.message()}, Receiving logs error: ${receivingLogsResponse.message()}"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to fetch logs: ${e.message}"
                    )
                }
            }
        }
    }
}
