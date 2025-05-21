package com.example.connect.data.repository

import android.content.Context
import android.util.Log
import com.example.connect.data.UserModel
import com.example.connect.domain.QRScannerRepository
import com.example.connect.utils.ApiResponse
import com.example.connect.utils.AppConstants.Companion.CONNECTUSERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QRScannerRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFireStore: FirebaseFirestore,
    @ApplicationContext private val context: Context,
): QRScannerRepository {
    private lateinit var firebaseAuth: FirebaseAuth
    override suspend fun createUser(userModel: UserModel): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)
        firebaseAuth = FirebaseAuth.getInstance()
        userModel.userId = firebaseAuth.currentUser?.uid.toString()
        try {
            val currentUserId = firebaseAuth.currentUser?.uid.orEmpty()
            if (currentUserId.isEmpty()) {
                emit(ApiResponse.Failure(Exception("User not logged in")))
                return@flow
            }
            firebaseFireStore.collection(CONNECTUSERS)
                .document(currentUserId)
                .set(userModel)
                .await()

            emit(ApiResponse.Success(true))
            Log.d("Upload firestore", "Successfully created for user: $currentUserId")
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e))
        }
    }

    override suspend fun getUser(): Flow<ApiResponse<UserModel>> = flow {
        emit(ApiResponse.Loading)
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = firebaseAuth.currentUser?.uid.orEmpty()
        if (currentUserId.isEmpty()) {
            emit(ApiResponse.Failure(Exception("User is not Logged In")))
            return@flow
        }

        try {
            val snapshot = firebaseFireStore
                .collection(CONNECTUSERS)
                .document(currentUserId)
                .get()
                .await()

            val user = snapshot.toObject(UserModel::class.java)
            if (user != null) {
                emit(ApiResponse.Success(user))
            } else {
                emit(ApiResponse.Failure(Exception("User data not found")))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e))
        }
    }

    override fun getUserById(userId: String): Flow<ApiResponse<UserModel>> = flow {
        emit(ApiResponse.Loading)
        try {
            val snap = firebaseFireStore
                .collection(CONNECTUSERS)
                .document(userId)
                .get()
                .await()
            val user = snap.toObject(UserModel::class.java)
            if (user != null) emit(ApiResponse.Success(user))
            else emit(ApiResponse.Failure(Exception("User not found")))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e))
        }
    }

}