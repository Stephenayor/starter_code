// Message.kt
package com.example.connect.data.model

data class Message(
    val sender: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
