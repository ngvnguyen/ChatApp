package com.sf.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sf.chatapp.remote.model.Content
import com.sf.chatapp.remote.model.Conversation
import com.sf.chatapp.remote.model.RequestBody
import com.sf.chatapp.remote.repository.GeminiRepository
import com.sf.chatapp.utils.FirebaseConstraint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatBotViewModel(private val geminiRepository: GeminiRepository): ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _messages = MutableStateFlow(listOf<Content>())
    val messages = _messages.asStateFlow()
    private val firestore = FirebaseFirestore.getInstance()
    private val conversationRef = firestore.collection(FirebaseConstraint.USER)
        .document(firebaseAuth.currentUser!!.uid)
        .collection(FirebaseConstraint.CHATBOT)

    private var currentConversation :DocumentReference?=null
    private val _conversations = MutableStateFlow<Map<String,Conversation>>(mapOf())
    val conversations = _conversations.asStateFlow()

    private val _searchConversation = MutableStateFlow<Map<String,Conversation>>(mapOf())
    val searchConversation = _searchConversation.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()


    init {

        conversationRef.addSnapshotListener { value, error ->
            if (error!=null){
                Log.d("ChatBotViewModel",error.message.toString())
                return@addSnapshotListener
            }
            value?.apply {
                val tempConversation = mutableMapOf<String,Conversation>()
                documents.map {document->
                    val conversation = document.toObject(Conversation::class.java)
                    conversation?.let { tempConversation.put(document.id, it) }
                }
                _conversations.value = tempConversation
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
            val geminiResponse = geminiRepository.sendMessage(requestBody)
            _messages.value = requestBody.contents + geminiResponse.candidates[0].content
            if (currentConversation==null){
                currentConversation = conversationRef.document()
            }

            val conversation = Conversation(
                content = _messages.value
            )
            currentConversation?.set(conversation)?.addOnFailureListener{
                Log.d("ChatBotViewModel",it.message.toString())
            }

            onSuccess()
        }catch (e:Exception){
            Log.d("ChatBotViewModel",e.message.toString())
            onFailed()
        }



    }

    fun createNewConversation(){
        currentConversation = null
        _messages.value = listOf()
    }

    fun selectConversation(key:String){
        currentConversation = conversationRef.document(key)
        _messages.value = conversations.value[key]?.content?: listOf()
    }

}

