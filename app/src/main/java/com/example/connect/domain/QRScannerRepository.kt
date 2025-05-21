package com.example.connect.domain

import android.net.Uri
import com.example.connect.data.UserModel
import com.example.connect.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface QRScannerRepository {

    suspend fun createUser(userModel: UserModel): Flow<ApiResponse<Boolean>>
    suspend fun getUser(): Flow<ApiResponse<UserModel>>
    fun getUserById(userId: String): Flow<ApiResponse<UserModel>>
}