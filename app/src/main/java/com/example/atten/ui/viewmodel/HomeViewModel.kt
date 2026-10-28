package com.example.atten.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atten.data.entity.AppConfigEntity
import com.example.atten.data.entity.LocationEntity
import com.example.atten.data.repository.ConfigRepository
import com.example.atten.data.repository.LocationRepository
import com.example.atten.data.repository.RequestResult
import com.example.atten.data.repository.RequestService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val config: AppConfigEntity? = null,
    val locations: List<LocationEntity> = emptyList(),
    val isLoading: Boolean = false,
    val requestInProgress: Boolean = false,
    val lastRequestResult: RequestResult? = null,
    val showResultSheet: Boolean = false,
    val errorMessage: String = ""
)

class HomeViewModel(
    private val configRepository: ConfigRepository,
    private val locationRepository: LocationRepository,
    private val requestService: RequestService
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadConfigAndLocations()
    }
    
    private fun loadConfigAndLocations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val config = configRepository.getConfig()
                if (config != null) {
                    val locations = locationRepository.getLocations(config.id)
                    _uiState.update { 
                        it.copy(
                            config = config,
                            locations = locations,
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
                        errorMessage = e.message ?: "Error loading config"
                    )
                }
            }
        }
    }
    
    fun sendRequest(location: LocationEntity) {
        viewModelScope.launch {
            _uiState.update { it.copy(requestInProgress = true) }
            try {
                val config = _uiState.value.config
                if (config != null) {
                    val result = requestService.sendRequest(config, location)
                    _uiState.update { 
                        it.copy(
                            requestInProgress = false,
                            lastRequestResult = result,
                            showResultSheet = true
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        requestInProgress = false,
                        errorMessage = e.message ?: "Error sending request"
                    )
                }
            }
        }
    }
    
    fun hideResultSheet() {
        _uiState.update { it.copy(showResultSheet = false) }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = "") }
    }
    
    fun navigateToConfig() {
        // This will be handled by navigation in the composable
    }
}
