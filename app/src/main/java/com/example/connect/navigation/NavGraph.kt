package com.example.connect.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.connect.presentation.chats.ChatsScreen
import com.example.connect.presentation.qrscanner.QRScannerHome
import com.example.connect.presentation.details.UserDetailsScreen
import com.example.connect.presentation.onboarding.LoginScreen
import com.example.connect.presentation.onboarding.SignUpScreen
import com.example.connect.presentation.qrscanner.QRScannerViewModel
import com.example.connect.presentation.qrscanner.ScanBarcodeScreen
import com.example.connect.utils.Route

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.SIGN_UP){
        composable(Route.SIGN_UP) {
            SignUpScreen( onSignUp = ({ _, _ -> }),
                navController
            )
        }
        composable(Route.SIGN_IN) {
            LoginScreen(
                onLogin = {_, _, ->},
                onGoogleSignIn = {}
            )
        }
        composable(Route.QR_SCANNER) {
            val qrScannerViewModel: QRScannerViewModel = hiltViewModel()
            QRScannerHome(modifier, qrScannerViewModel, navController)
        }
        composable(Route.SCAN_BARCODE) {
            val qrScannerViewModel: QRScannerViewModel = hiltViewModel()
            ScanBarcodeScreen (qrScannerViewModel, onResult = {}, navController)
        }
        composable(Route.USER_DETAILS) {
            UserDetailsScreen(modifier, navController)
        }
        composable(Route.CHATS_SCREEN) {
            ChatsScreen()
        }
    }
}