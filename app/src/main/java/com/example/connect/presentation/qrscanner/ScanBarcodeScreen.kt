package com.example.connect.presentation.qrscanner

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.connect.R
import com.example.connect.data.model.UserModel
import com.example.connect.utils.ApiResponse
import com.example.connect.utils.Route
import com.example.connect.utils.Tools
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.integration.android.IntentIntegrator
import java.io.InputStream

@Composable
fun ScanBarcodeScreen(
    viewModel: QRScannerViewModel = hiltViewModel(),
    onResult: (UserModel) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    fun Context.findActivity(): Activity? {
        var ctx = this
        while (ctx is ContextWrapper) {
            if (ctx is Activity) return ctx
            ctx = ctx.baseContext
        }
        return null
    }

    val activity = context.findActivity()
    val scanState by viewModel.scanState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    // Launcher for Camera Scan
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val barCodeDecoder = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
        barCodeDecoder.contents?.let { viewModel.scanBarCode(it) }
    }

    // Launcher for Gallery
    val galleryLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri: Uri? ->
        uri?.let {
            // decode bitmap from uri
            val bitmap = loadBitmapFromUri(context, it)
            bitmap?.let { bmp ->
                // convert to ZXing BinaryBitmap
                val width = bmp.width
                val height = bmp.height
                val pixels = IntArray(width * height)
                bmp.getPixels(pixels, 0, width, 0, 0, width, height)
                val source: LuminanceSource = RGBLuminanceSource(width, height, pixels)
                val binary = BinaryBitmap(HybridBinarizer(source))
                try {
                    val barCodeDecodingResult = MultiFormatReader().decode(binary)
                    viewModel.scanBarCode(barCodeDecodingResult.text)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
        }
    }

    Scaffold(
        Modifier.padding(top = 20.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Select scan mode:", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))
            Button(
                //Camera
                onClick = {
                    IntentIntegrator(activity)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setOrientationLocked(false)
                        .createScanIntent().also { intent ->
                            cameraLauncher.launch(intent)
                        }
                },
                colors = ButtonDefaults.buttonColors(Color.Gray),
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Camera",
                        tint = Color.Unspecified,
                        modifier = Modifier.height(40.dp)
                    )
                    Text("Camera", color = Color.White, modifier = Modifier.padding(8.dp))
                }
            }
            Spacer(Modifier.height(12.dp))

            Button(onClick = {
                //Gallery
                galleryLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            },
                colors = ButtonDefaults.buttonColors(Color.Gray),
                modifier = Modifier.padding(8.dp)
                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.uploadsimple),
                        contentDescription = "Camera",
                        tint = Color.Unspecified,
                        modifier = Modifier.height(40.dp)
                    )
                    Text("Upload from Gallery", color = Color.White, modifier = Modifier.padding(8.dp))
                }
            }

            Spacer(Modifier.height(24.dp))
            if (isLoading) CircularProgressIndicator()

            when (scanState) {
                is ApiResponse.Loading -> isLoading = true

                is ApiResponse.Success -> {
                    isLoading = false
                    val user = (scanState as ApiResponse.Success<UserModel>).data
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Name: ${user?.accountName}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Account Number: ${user?.accountNumber}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "User ID: ${user?.userId}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = {
                            if (user != null) {
                                onResult(user)
                                navController.navigate(Route.QR_SCANNER){
                                    popUpTo(Route.SCAN_BARCODE){inclusive = true}
                                }
                            }
                        }) {
                            Text("Continue")
                        }
                    }
                }

                is ApiResponse.Failure -> {
                    isLoading = false
                    val errorMessage = (scanState as ApiResponse.Failure).e
                    Tools.showToast(context, errorMessage?.localizedMessage ?: "Unknown")
                }

                else -> {
                    //                Text("Awaiting scan...", color = Color.Gray)
                }
            }
        }
    }
}


/**
 * Helper to load Bitmap from Uri (gallery pick)
 */
private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val stream: InputStream? = context.contentResolver.openInputStream(uri)
        android.graphics.BitmapFactory.decodeStream(stream)
    } catch (e: Exception) {
        null
    }
}


@Preview
@Composable
private fun ScanBarcodeScreenPreview() {

}
