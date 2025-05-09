package com.example.connect.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.connect.utils.Route

@Composable
fun QRScannerHome(modifier: Modifier = Modifier,
                  navController: NavController) {
    val context = LocalContext.current


    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column (
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
        ){

        }

    }

}


@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Reports")
    val icons = listOf(
        rememberVectorPainter(image = Icons.Default.QrCodeScanner),
        rememberVectorPainter(image = Icons.Default.List),
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    when (item) {
                        "QR Scan" -> navController.navigate(Route.QR_SCANNER) {
                            popUpTo(Route.QR_SCANNER) { inclusive = true }
                        }

                        "Reports" -> navController.navigate(Route.QR_SCANNER) {
                            popUpTo(Route.QR_SCANNER)
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = icons[index],
                        contentDescription = item,
                        tint = if (selectedItem == index) Color.Unspecified else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item,
                        color = if (selectedItem == index) Color.Blue else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Unspecified,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Red,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}