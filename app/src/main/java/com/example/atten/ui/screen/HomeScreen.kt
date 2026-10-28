package com.example.atten.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.atten.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToConfig: () -> Unit,
    onNavigateToLogs: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Atten") },
                actions = {
                    IconButton(onClick = onNavigateToLogs) {
                        Text("📋")
                    }
                    IconButton(onClick = onNavigateToConfig) {
                        Text("⚙️")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.config == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "No Configuration Found",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Button(
                        onClick = onNavigateToConfig,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Configure Now")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        "Configured Locations",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                
                if (uiState.locations.isEmpty()) {
                    item {
                        Text(
                            "No locations configured. Go to settings to add locations.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(uiState.locations) { location ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    location.locationName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    "Lat: ${String.format("%.6f", location.latitude)}, " +
                                        "Lon: ${String.format("%.6f", location.longitude)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Button(
                                    onClick = { viewModel.sendRequest(location) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    enabled = !uiState.requestInProgress
                                ) {
                                    if (uiState.requestInProgress) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    } else {
                                        Text("Send Request")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Result Bottom Sheet
        if (uiState.showResultSheet && uiState.lastRequestResult != null) {
            ResultBottomSheet(
                result = uiState.lastRequestResult!!,
                onDismiss = { viewModel.hideResultSheet() }
            )
        }
        
        // Error Snackbar
        if (uiState.errorMessage.isNotEmpty()) {
            LaunchedEffect(uiState.errorMessage) {
                // Show snackbar logic here
                viewModel.clearError()
            }
        }
    }
}

@Composable
fun ResultBottomSheet(
    result: com.example.atten.data.repository.RequestResult,
    onDismiss: () -> Unit
) {
    var showRawResponse by remember { mutableStateOf(false) }
    
    ModalBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Request Result",
                style = MaterialTheme.typography.headlineSmall
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Status
            Card(
                modifier = Modifier.fillMaxWidth(),
                containerColor = if (result.isSuccess) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    MaterialTheme.colorScheme.errorContainer
                }
            ) {
                Text(
                    if (result.isSuccess) "✓ Success" else "✗ Failed",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Details
            Text(
                "Status Code: ${result.statusCode}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            if (result.isNetworkError) {
                Text(
                    "Network Error: ${result.errorMessage}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            Text(
                "Used Location: Lat ${String.format("%.6f", result.usedLatitude)}, " +
                    "Lon ${String.format("%.6f", result.usedLongitude)}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Raw Response Toggle
            Button(
                onClick = { showRawResponse = !showRawResponse },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showRawResponse) "Hide Raw Response" else "Show Raw Response")
            }
            
            if (showRawResponse) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text(
                        result.responseBody,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
