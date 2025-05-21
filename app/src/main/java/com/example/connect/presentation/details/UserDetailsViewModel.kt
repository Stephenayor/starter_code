package com.example.connect.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connect.data.UserModel
import com.example.connect.domain.QRScannerRepository
import com.example.connect.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val qrScannerRepository: QRScannerRepository
): ViewModel() {

    private val _createUserResponse = MutableStateFlow<ApiResponse<Boolean>>(ApiResponse.Idle)
    val createUserResponse: StateFlow<ApiResponse<Boolean>> = _createUserResponse

    private val _getUserResponse = MutableStateFlow<ApiResponse<UserModel>>(ApiResponse.Idle)
    val getUserResponse: StateFlow<ApiResponse<UserModel>> = _getUserResponse



    fun createUser(userModel: UserModel){
        _createUserResponse.value = ApiResponse.Loading
        viewModelScope.launch {
            qrScannerRepository.createUser(userModel).collect { response ->
                _createUserResponse.value = response
            }
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

    fun clearLoadingState() {
        _createUserResponse.value = ApiResponse.Idle
    }
}