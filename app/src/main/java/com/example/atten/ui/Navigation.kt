package com.example.atten.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atten.ui.screen.ConfigScreen
import com.example.atten.ui.screen.HomeScreen
import com.example.atten.ui.screen.LogScreen
import com.example.atten.ui.viewmodel.ConfigViewModel
import com.example.atten.ui.viewmodel.HomeViewModel
import com.example.atten.ui.viewmodel.LogViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Config : Screen("config")
    object Logs : Screen("logs")
}

@Composable
fun AttenNavigation(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel,
    configViewModel: ConfigViewModel,
    logViewModel: LogViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToConfig = {
                    navController.navigate(Screen.Config.route)
                },
                onNavigateToLogs = {
                    navController.navigate(Screen.Logs.route)
                }
            )
        }
        
        composable(Screen.Config.route) {
            ConfigScreen(
                viewModel = configViewModel,
                onSaved = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Logs.route) {
            LogScreen(
                viewModel = logViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
