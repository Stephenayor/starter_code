package com.example.connect.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.connect.presentation.QRScannerHome
import com.example.connect.presentation.details.UserDetailsScreen
import com.example.connect.presentation.onboarding.LoginScreen
import com.example.connect.presentation.onboarding.SignUpScreen
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
            QRScannerHome(modifier, navController)
        }
        composable(Route.USER_DETAILS) {
            UserDetailsScreen(modifier, navController)
        }
    }
}