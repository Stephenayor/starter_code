package com.example.connect.presentation.qrscanner

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connect.data.model.UserModel
import com.example.connect.domain.QRScannerRepository
import com.example.connect.utils.ApiResponse
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel @Inject constructor(
    private val qrScannerRepository: QRScannerRepository
): ViewModel() {

    private val _barcodeBitmap = MutableStateFlow<Bitmap?>(null)
    val barcodeBitmap: StateFlow<Bitmap?> = _barcodeBitmap

    private val _scanState = MutableStateFlow<ApiResponse<UserModel>>(ApiResponse.Idle)
    val scanState: StateFlow<ApiResponse<UserModel>> = _scanState

    private val _getUserResponse = MutableStateFlow<ApiResponse<UserModel>>(ApiResponse.Idle)
    val getUserResponse: StateFlow<ApiResponse<UserModel>> = _getUserResponse

    private val gson = Gson()

    //Generate QR code from userModel object
    fun generateBarcode(user: UserModel) {
        viewModelScope.launch {
            val json = gson.toJson(user)
            val matrix = MultiFormatWriter()
                .encode(json, BarcodeFormat.QR_CODE, 400, 400)
            val bmp = BarcodeEncoder().createBitmap(matrix)
            _barcodeBitmap.value = bmp
        }
    }

    fun getUserDetails(){
        _getUserResponse.value = ApiResponse.Loading
        viewModelScope.launch {
            qrScannerRepository.getUser().collect { response ->
                _getUserResponse.value = response
            }
        }
    }

    fun scanBarCode(barCodeData: String) {
        viewModelScope.launch {
            _scanState.value = ApiResponse.Loading
            val barcodeString = barCodeData.trim()
            when {
                //JSON payload — try to deserialize
                barcodeString.startsWith("{") && barcodeString.endsWith("}") -> {
                    try {
                        val user = gson.fromJson(barcodeString, UserModel::class.java)
                        _scanState.value = ApiResponse.Success(user)
                    } catch (e: Exception) {
                        _scanState.value = ApiResponse.Failure(e)
                    }
                }

                //Plain userId — fetch from Firebase
                barcodeString.isNotEmpty() -> {
                    qrScannerRepository.getUserById(barcodeString)
                        .collect { response ->
                            _scanState.value = response
                        }
                }
                else -> {
                    _scanState.value = ApiResponse.Failure(
                        IllegalArgumentException("Unrecognized barcode format")
                    )
                }
            }
        }
    }
}