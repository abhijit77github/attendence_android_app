package com.example.atten.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atten.data.entity.RequestLogEntity
import com.example.atten.data.repository.ConfigRepository
import com.example.atten.data.repository.RequestLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LogUiState(
    val logs: List<RequestLogEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class LogViewModel(
    private val configRepository: ConfigRepository,
    private val requestLogRepository: RequestLogRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LogUiState())
    val uiState: StateFlow<LogUiState> = _uiState.asStateFlow()
    
    init {
        loadLogs()
    }
    
    private fun loadLogs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val config = configRepository.getConfig()
                if (config != null) {
                    val logs = requestLogRepository.getRequestLogsLast7Days(config.id)
                    _uiState.update { 
                        it.copy(
                            logs = logs,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error loading logs"
                    )
                }
            }
        }
    }
    
    fun refreshLogs() {
        loadLogs()
    }
}
