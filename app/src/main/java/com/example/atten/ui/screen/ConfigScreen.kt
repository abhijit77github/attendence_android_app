package com.example.atten.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.atten.ui.viewmodel.ConfigUiState
import com.example.atten.ui.viewmodel.ConfigViewModel

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel,
    onSaved: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Configure App") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "App Configuration",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            
            // Endpoint URL
            item {
                OutlinedTextField(
                    value = uiState.config.endpoint,
                    onValueChange = { viewModel.updateEndpoint(it) },
                    label = { Text("Endpoint URL") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            
            // HTTP Method
            item {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = uiState.config.httpMethod,
                        onValueChange = { },
                        label = { Text("HTTP Method") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("GET") },
                            onClick = {
                                viewModel.updateHttpMethod("GET")
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("POST") },
                            onClick = {
                                viewModel.updateHttpMethod("POST")
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            // User ID
            item {
                OutlinedTextField(
                    value = uiState.config.userId,
                    onValueChange = { viewModel.updateUserId(it) },
                    label = { Text("User ID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            
            // User Email
            item {
                OutlinedTextField(
                    value = uiState.config.userEmail,
                    onValueChange = { viewModel.updateUserEmail(it) },
                    label = { Text("User Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
            }
            
            // Success Criteria
            item {
                Text(
                    "Success Criteria",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            item {
                OutlinedTextField(
                    value = uiState.config.successCriteriaJsonPath,
                    onValueChange = { path ->
                        viewModel.updateSuccessCriteria(
                            path,
                            uiState.config.successCriteriaValue
                        )
                    },
                    label = { Text("JSON Path (e.g., response.status)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            
            item {
                OutlinedTextField(
                    value = uiState.config.successCriteriaValue,
                    onValueChange = { value ->
                        viewModel.updateSuccessCriteria(
                            uiState.config.successCriteriaJsonPath,
                            value
                        )
                    },
                    label = { Text("Expected Value (e.g., success)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            
            // Locations Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Locations (${uiState.locations.size})",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(onClick = { /* Open add location dialog */ }) {
                        Text("+")
                    }
                }
            }
            
            // Locations List
            items(uiState.locations.size) { index ->
                val location = uiState.locations[index]
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                location.locationName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "Lat: ${String.format("%.6f", location.latitude)}, " +
                                    "Lon: ${String.format("%.6f", location.longitude)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(onClick = { viewModel.deleteLocation(location) }) {
                            Text("✕")
                        }
                    }
                }
            }
            
            // Save Button
            item {
                Button(
                    onClick = {
                        viewModel.saveConfig()
                        onSaved()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    } else {
                        Text("Save Configuration")
                    }
                }
            }
            
            // Error Message
            if (uiState.errorMessage.isNotEmpty()) {
                item {
                    Text(
                        uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
