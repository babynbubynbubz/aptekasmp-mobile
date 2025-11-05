package com.example.aptekaspm_mobile.ui.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aptekaspm_mobile.data.network.ApiService
import com.example.aptekaspm_mobile.data.network.models.ReceiveLogItem
import com.example.aptekaspm_mobile.data.network.models.RestockLogItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogsScreenState(
    val isLoading: Boolean = false,
    val restockLogs: List<RestockLogItem> = emptyList(),
    val receiveLogs: List<ReceiveLogItem> = emptyList(),
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
                // In a real app, you would make the network calls here.
                // For now, we'll simulate a successful response with dummy data.

                // FAKE RESPONSE FOR DEVELOPMENT
                val fakeRestockLogs = listOf(
                    RestockLogItem(
                        1,
                        101,
                        10,
                        "2023-10-27T10:00:00Z",
                        "Super Med",
                        "SN67890",
                        "2025-12-31"
                    ),
                    RestockLogItem(
                        2,
                        102,
                        5,
                        "2023-10-27T11:00:00Z",
                        "Another Med",
                        "SNABCDE",
                        "2024-11-30"
                    )
                )
                val fakeReceiveLogs = listOf(
                    ReceiveLogItem(1, "2023-10-26T09:00:00Z", "Super Med", "SN67890", "2025-12-31"),
                    ReceiveLogItem(
                        2,
                        "2023-10-26T09:30:00Z",
                        "Another Med",
                        "SNABCDE",
                        "2024-11-30"
                    )
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        restockLogs = fakeRestockLogs,
                        receiveLogs = fakeReceiveLogs
                    )
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
