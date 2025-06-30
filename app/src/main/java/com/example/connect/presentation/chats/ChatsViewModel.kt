package com.example.connect.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connect.data.model.Message
import com.example.connect.domain.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val chatRepository: ChatRepository
): ViewModel(){

    private val _chatMessages = MutableStateFlow<List<Message>>(emptyList())
    val chatMessages: StateFlow<List<Message>> = _chatMessages

    init {
        chatRepository.connect()
        viewModelScope.launch {
            chatRepository.messages.collect { message ->
                _chatMessages.update { it + message }
            }
        }
    }

    fun send(message: String){
        chatRepository.sendMessage(Message(sender = "You", content = message))
    }

    override fun onCleared() {
        chatRepository.disconnect()
        super.onCleared()
    }
}