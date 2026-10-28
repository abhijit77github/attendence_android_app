package com.example.atten.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atten.data.entity.AppConfigEntity
import com.example.atten.data.entity.LocationEntity
import com.example.atten.data.repository.ConfigRepository
import com.example.atten.data.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConfigUiState(
    val config: AppConfigEntity = AppConfigEntity(),
    val locations: List<LocationEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class ConfigViewModel(
    private val configRepository: ConfigRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConfigUiState())
    val uiState: StateFlow<ConfigUiState> = _uiState.asStateFlow()
    
    init {
        loadConfig()
    }
    
    private fun loadConfig() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val config = configRepository.getConfig() ?: AppConfigEntity()
                val locations = locationRepository.getLocations(config.id)
                _uiState.update { 
                    it.copy(
                        config = config,
                        locations = locations,
                        isLoading = false
                    )
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
    
    fun updateEndpoint(endpoint: String) {
        _uiState.update { 
            it.copy(config = it.config.copy(endpoint = endpoint))
        }
    }
    
    fun updateHttpMethod(method: String) {
        _uiState.update { 
            it.copy(config = it.config.copy(httpMethod = method))
        }
    }
    
    fun updateUserId(userId: String) {
        _uiState.update { 
            it.copy(config = it.config.copy(userId = userId))
        }
    }
    
    fun updateUserEmail(email: String) {
        _uiState.update { 
            it.copy(config = it.config.copy(userEmail = email))
        }
    }
    
    fun updateSuccessCriteria(jsonPath: String, value: String) {
        _uiState.update { 
            it.copy(config = it.config.copy(
                successCriteriaJsonPath = jsonPath,
                successCriteriaValue = value
            ))
        }
    }
    
    fun updateFailureCriteria(jsonPath: String, value: String) {
        _uiState.update { 
            it.copy(config = it.config.copy(
                failureCriteriaJsonPath = jsonPath,
                failureCriteriaValue = value
            ))
        }
    }
    
    fun addLocation(locationName: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val newLocation = LocationEntity(
                    configId = _uiState.value.config.id,
                    locationName = locationName,
                    latitude = latitude,
                    longitude = longitude
                )
                locationRepository.addLocation(newLocation)
                loadConfig()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "Error adding location: ${e.message}")
                }
            }
        }
    }
    
    fun deleteLocation(location: LocationEntity) {
        viewModelScope.launch {
            try {
                locationRepository.deleteLocation(location)
                _uiState.update { 
                    it.copy(locations = it.locations.filter { l -> l.id != location.id })
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "Error deleting location: ${e.message}")
                }
            }
        }
    }
    
    fun saveConfig() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                configRepository.saveConfig(_uiState.value.config)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error saving config: ${e.message}"
                    )
                }
            }
        }
    }
}
