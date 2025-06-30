package com.example.connect.domain

import com.example.connect.data.model.UserModel
import com.example.connect.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface QRScannerRepository {

    suspend fun createUser(userModel: UserModel): Flow<ApiResponse<Boolean>>
    suspend fun getUser(): Flow<ApiResponse<UserModel>>
    fun getUserById(userId: String): Flow<ApiResponse<UserModel>>
}