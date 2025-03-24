package com.sf.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.sf.chatapp.remote.model.Content
import com.sf.chatapp.remote.model.Conversation
import com.sf.chatapp.remote.model.RequestBody
import com.sf.chatapp.remote.repository.ChatBotRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatBotViewModel(
    private val chatBotRepository: ChatBotRepository
): ViewModel() {
    private val _messages = MutableStateFlow(listOf<Content>())
    val messages = _messages.asStateFlow()

    private var currentConversation :DocumentReference
    private val _conversations = MutableStateFlow<Map<String,Conversation>>(mapOf())

    private val _searchConversation = MutableStateFlow<Map<String,Conversation>>(mapOf())
    val searchConversation = _searchConversation.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()


    init {
        currentConversation = chatBotRepository.getNewConversation()

        viewModelScope.launch {
            chatBotRepository.getConversationMapFlow().collectLatest {
                _conversations.value = it
            }
        }

        viewModelScope.launch {
            _searchQuery.collectLatest {query->
                if (query.isEmpty()) _searchConversation.update {  _searchConversation.value }
                else _searchConversation.update { _conversations.value.filter { (_,conversation)->
                    conversation.content.any{it.parts[0].text.contains(query)}
                } }
            }
        }
        viewModelScope.launch {
            _conversations.collectLatest {conversations->
                with(_searchQuery){
                    if (value.isEmpty()) _searchConversation.update {  conversations }
                    else _searchConversation.update {conversations.filter { (_,conversation)->
                        conversation.content.any{it.parts[0].text.contains(value)}
                    } }

                }


            }
        }
    }

    fun searchConversation(query:String){
        _searchQuery.update { query }
    }

    suspend fun sendMessage(
        message:String,
        onSuccess:()->Unit = {},
        onFailed:()->Unit ={}
    ){

        try{
            val requestBody = RequestBody.builder(_messages.value,message)
            _messages.value = chatBotRepository.sendMessage(
                currentConversation,
                requestBody
            )
            onSuccess()
        }catch (e:Exception){
            Log.d("ChatBotViewModel",e.message.toString())
            onFailed()
        }

    }

    fun createNewConversation(){
        currentConversation = chatBotRepository.getNewConversation()
        _messages.value = listOf()
    }

    fun selectConversation(key:String){
        currentConversation = chatBotRepository.getConversation(key)
        _messages.value = _conversations.value[key]?.content?: listOf()
    }

}

