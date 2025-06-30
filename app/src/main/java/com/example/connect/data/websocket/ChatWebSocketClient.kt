
package com.example.connect.data.websocket
import okhttp3.*
import okio.ByteString

class ChatWebSocketClient(
    private val listener: WebSocketListener
) {
    private val client = OkHttpClient()

    fun connect(url: String): WebSocket {
        val request = Request.Builder().url(url).build()
        return client.newWebSocket(request, listener)
    }

    fun close(webSocket: WebSocket) {
        webSocket.close(1000, "Closing")
    }
}
