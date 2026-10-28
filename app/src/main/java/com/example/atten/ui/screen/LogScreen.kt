package com.example.atten.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.atten.data.entity.RequestLogEntity
import com.example.atten.ui.viewmodel.LogViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LogScreen(
    viewModel: LogViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request History (Last 7 Days)") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←")
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
        } else if (uiState.logs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No request logs found",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.logs) { log ->
                    LogEntryCard(log)
                }
            }
        }
    }
}

@Composable
fun LogEntryCard(log: RequestLogEntity) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        log.locationName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        formatTimestamp(log.timestamp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                // Status Badge
                Surface(
                    modifier = Modifier.fillMaxWidth(0.3f),
                    color = if (log.isSuccess) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        if (log.isSuccess) "✓ Success" else "✗ Failed",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Details
            Text(
                "Status Code: ${log.statusCode}",
                style = MaterialTheme.typography.bodySmall
            )
            
            Text(
                "Used Coordinates: Lat ${String.format("%.6f", log.usedLatitude)}, " +
                    "Lon ${String.format("%.6f", log.usedLongitude)}",
                style = MaterialTheme.typography.bodySmall
            )
            
            Text(
                "Method: ${log.httpMethod}",
                style = MaterialTheme.typography.bodySmall
            )
            
            if (log.isNetworkError) {
                Text(
                    "Network Error: ${log.errorMessage}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            // Expand/Collapse Button
            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(if (expanded) "Hide Details" else "Show Details")
            }
            
            // Details Section
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            "Request URL: ${log.requestUrl}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        
                        if (log.requestBody.isNotEmpty()) {
                            Text(
                                "Request Body:",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                log.requestBody,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        
                        if (log.responseBody.isNotEmpty()) {
                            Text(
                                "Response:",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                log.responseBody,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
