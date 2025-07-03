package com.example.connect.presentation.qrscanner

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.connect.data.model.UserModel
import com.example.connect.utils.ApiResponse
import com.example.connect.utils.Route
import com.example.connect.utils.Tools

@Composable
fun QRScannerHome(
    modifier: Modifier = Modifier,
    viewModel: QRScannerViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val userResponse by viewModel.getUserResponse.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var userModel: UserModel? = null


    LaunchedEffect(Unit) {
//        navController.popBackStack()
        viewModel.getUserDetails()
//        userModel?.let { viewModel.generateBarcode(it) }
    }

    val bitmap by viewModel.barcodeBitmap.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBarWithCenterChat(navController) },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surfaceBright,
                onClick = {
                    navController.navigate(Route.USER_DETAILS)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {

            if (isLoading) {
                CircularProgressIndicator()
            }

            Text("Your QR Code", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Account QR Code",
                    modifier = Modifier.size(250.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Show user details
            userModel?.let { user ->
                Text(text = "Name: ${user.accountName}", style = MaterialTheme.typography.bodyLarge)

                Spacer(Modifier.height(16.dp))

                // Share Button
                TextButton(onClick = {
                    bitmap?.let { Tools.shareBitmap(context, it, "${user.accountName}_QRCode") }
                }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share QR Code",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Share")
                }
            }



            when (val state = userResponse) {
                is ApiResponse.Idle -> {

                }

                is ApiResponse.Loading -> {
                    isLoading = true

                }

                is ApiResponse.Success -> {
                    isLoading = false
                    userModel = state.data
                    userModel?.let { viewModel.generateBarcode(it) }
                }

                is ApiResponse.Failure -> {
                    isLoading = false
                    Tools.showToast(context, state.e?.message)
                }
            }

        }

    }

}


@Composable
fun BottomNavigationBarWithCenterChat(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Charts", "Chat", "Scan")

    Box {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 4.dp,
            modifier = Modifier
                .height(72.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        ) {
            items.forEachIndexed { index, item ->
                if (index == 2) {
                    Spacer(modifier = Modifier.weight(2f))
                } else {
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            when (item) {
                                "Home" -> navController.navigate(Route.QR_SCANNER)
                                "Charts" -> navController.navigate(Route.CRYPTO_SCREEN)
                                "Scan" -> navController.navigate(Route.SCAN_BARCODE)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = when (item) {
                                    "Home" -> Icons.Default.QrCodeScanner
                                    "Charts" -> Icons.Default.ShowChart
                                    "Scan" -> Icons.Default.List
                                    else -> Icons.Default.Info
                                },
                                contentDescription = item,
                                tint = if (selectedItem == index) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                text = item,
                                color = if (selectedItem == index) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                selectedItem = 2 // Chat index
                navController.navigate(Route.CHATS_SCREEN)
            },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-24).dp)
        ) {
            Icon(imageVector = Icons.Default.Chat, contentDescription = "Chat")
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Charts", "Scan")
    val icons = listOf(
        rememberVectorPainter(image = Icons.Default.QrCodeScanner),
        rememberVectorPainter(image = Icons.Default.List),
        rememberVectorPainter(image = Icons.Default.BarChart),
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    when (item) {
                        "Home" -> navController.navigate(Route.QR_SCANNER) {
                            popUpTo(Route.QR_SCANNER) { inclusive = true }
                        }

                        "Scan" -> navController.navigate(Route.SCAN_BARCODE) {
                            popUpTo(Route.SCAN_BARCODE)
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
