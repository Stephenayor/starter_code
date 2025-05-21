package com.example.connect.utils

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Tools {

    companion object{
        fun showToast(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun shareBitmap(context: Context, bitmap: Bitmap, fileName: String) {
            val file = File(context.cacheDir, "$fileName.png")
            try {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }

                val uri = FileProvider.getUriForFile(
                    context,
                    "com.example.connect.provider",
                    file
                )

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                context.startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
            } catch (e: IOException) {
                Toast.makeText(context, "Error sharing QR code", Toast.LENGTH_SHORT).show()
            }
        }

//        fun shareBitmap(
//            context: Context,
//            bitmap: Bitmap,
//            fileName: String
//        ) {
//            // 1) Write the bitmap to cache/
//            val imagesFolder = File(context.cacheDir, "shared_images").apply { mkdirs() }
//            val imageFile = File(imagesFolder, "$fileName.png")
//            try {
//                FileOutputStream(imageFile).use { out ->
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
//                }
//            } catch (e: IOException) {
//                Toast.makeText(context, "Error preparing image for sharing", Toast.LENGTH_SHORT).show()
//                return
//            }
//
//            // 2) Get the content URI
//            val uri = FileProvider.getUriForFile(
//                context,
//                "${context.packageName}.provider",
//                imageFile
//            )
//
//            // 3) Build and fire the share intent
//            val shareIntent = Intent(Intent.ACTION_SEND).apply {
//                type = "image/png"
//                putExtra(Intent.EXTRA_STREAM, uri)
//                clipData = ClipData.newUri(context.contentResolver, "Image", uri)
//                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            }
//            context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
//        }


    }
}