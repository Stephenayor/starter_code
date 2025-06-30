
package com.example.connect.domain


import com.example.connect.data.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val messages: Flow<Message>
    fun sendMessage(message: Message)
    fun connect()
    fun disconnect()
}
