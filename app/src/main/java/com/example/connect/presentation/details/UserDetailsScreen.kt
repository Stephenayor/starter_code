package com.example.connect.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.connect.data.UserModel
import com.example.connect.utils.ApiResponse
import com.example.connect.utils.Route
import com.example.connect.utils.Tools
import com.example.connect.utils.dialogs.StatusDialog

@Composable
fun UserDetailsScreen(modifier: Modifier = Modifier,
                      navController: NavController,
                      userDetailsViewModel: UserDetailsViewModel = hiltViewModel(),) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var accountNumber by remember { mutableStateOf(TextFieldValue("")) }
    val createUserResponse by userDetailsViewModel.createUserResponse.collectAsState()
    val isButtonEnabled = name.text.isNotEmpty() &&
            accountNumber.text.isNotEmpty()
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.background(androidx.compose.material3.MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.Start
        ) {

            // App Logo and Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Replace with your logo
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFF3366FF), RoundedCornerShape(6.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Header
            Text(
                text = "Please Enter your Details",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (isLoading){
                Box(
                    Modifier.fillMaxSize()
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }

            // Name Field
            Text(
                text = "Name",
                color = Color(0xFF1D2433),
                fontFamily = FontFamily.SansSerif,
                fontSize = 15.sp
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter your name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Account Number",
                color = Color(0xFF1D2433),
                fontFamily = FontFamily.SansSerif,
                fontSize = 15.sp
            )
            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text("Enter your account number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(90.dp))

            Button(
                onClick = {
                    userDetailsViewModel.createUser(
                        UserModel(
                            name.text,
                            accountNumber.text,
                            ""
                        )
                    )
                },
                enabled = isButtonEnabled,
                colors = buttonColors(
                    containerColor = if (isButtonEnabled) Color(0xFF0D6EFD) else Color(0xFF0E7F0FF),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(top = 20.dp)
            ) {
                Text(text = "Continue")
            }

            when (val state = createUserResponse) {
                is ApiResponse.Idle -> {
                    // Do nothing; no feedback to the user yet
                }

                is ApiResponse.Loading -> {
                    isLoading = true
                }

                is ApiResponse.Success -> {
                    isLoading = false
                    name = TextFieldValue("")
                    accountNumber = TextFieldValue("")
                    StatusDialog(
                        show        = showDialog,
                        isSuccess   = true,
                        title       = "Account Created",
                        description = "Your operation completed successfully.",
                        buttonText  = "Generate your QR NOW",
                        onDismiss   = { showDialog = false },         // tap outside or back
                        onConfirm   = {
                            showDialog = false
                            /* additional action */
                            navController.navigate(Route.QR_SCANNER)
                        }
                    )

                }

                is ApiResponse.Failure -> {
                    isLoading = false
                    Tools.showToast(context, state.e?.message)
                }
            }

        }
    }
}


@Preview
@Composable
private fun UserDetailsScreenPreview() {
    val navController = rememberNavController()
    val userDetailsViewModel: UserDetailsViewModel = hiltViewModel()
    UserDetailsScreen(modifier = Modifier,navController)
}