package com.example.connect.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.connect.presentation.QRScannerHome
import com.example.connect.utils.Route

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.QR_SCANNER){
        composable(Route.QR_SCANNER) {
            QRScannerHome(modifier, navController)
        }
    }
}