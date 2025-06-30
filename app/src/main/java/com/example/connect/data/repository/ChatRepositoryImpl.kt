package com.example.connect.data.repository

import android.util.Log
import androidx.room.util.joinIntoString
import com.example.connect.data.model.Message
import com.example.connect.domain.ChatRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatRepositoryImpl(
    private val okHttpClient: OkHttpClient
) : ChatRepository {

    private var webSocket: WebSocket? = null
    private val messageChannel = Channel<Message>(Channel.BUFFERED)
    override val messages: Flow<Message> = messageChannel.receiveAsFlow()

    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("WebSocket", "Connected")
            webSocket.send("Hello from Android!")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "Message Received: $text")
            val message = Message(sender = "Server", content = text)
            messageChannel.trySend(message)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
            Log.d("WebSocket", "Closing: $code $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("WebSocket", "Error: ${t.message}")
        }
    }
    override fun sendMessage(message: Message) {
        val messageJson = message.content
        webSocket?.send(messageJson)
        messageChannel.trySend(message)
    }

    override fun connect() {
        if (webSocket != null) return

        val request = Request.Builder()
            .url("wss://echo.websocket.org")
            .build()
        webSocket = okHttpClient.newWebSocket(request,listener)
    }


    override fun disconnect() {
        webSocket?.close(1000, "User left")
        webSocket = null
    }
}