package com.example.connect.data.repository

import android.content.Context
import com.example.connect.domain.QRScannerRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class QRScannerRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFireStore: FirebaseFirestore,
    @ApplicationContext private val context: Context,
): QRScannerRepository {
}