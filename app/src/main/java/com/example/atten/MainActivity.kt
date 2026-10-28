package com.example.atten

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.atten.data.database.AttenDatabase
import com.example.atten.data.repository.ConfigRepository
import com.example.atten.data.repository.LocationRepository
import com.example.atten.data.repository.RequestLogRepository
import com.example.atten.data.repository.RequestService
import com.example.atten.ui.AttenNavigation
import com.example.atten.ui.theme.AttenTheme
import com.example.atten.ui.viewmodel.ConfigViewModel
import com.example.atten.ui.viewmodel.HomeViewModel
import com.example.atten.ui.viewmodel.LogViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize database and repositories
        val database = AttenDatabase.getDatabase(this)
        val configRepository = ConfigRepository(database.appConfigDao())
        val locationRepository = LocationRepository(database.locationDao())
        val requestLogRepository = RequestLogRepository(database.requestLogDao())
        val requestService = RequestService(configRepository, locationRepository, requestLogRepository)
        
        // Initialize ViewModels
        val homeViewModel = HomeViewModel(configRepository, locationRepository, requestService)
        val configViewModel = ConfigViewModel(configRepository, locationRepository)
        val logViewModel = LogViewModel(configRepository, requestLogRepository)
        
        setContent {
            AttenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AttenNavigation(
                        homeViewModel = homeViewModel,
                        configViewModel = configViewModel,
                        logViewModel = logViewModel
                    )
                }
            }
        }
    }
}
