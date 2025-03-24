package com.sf.chatapp.remote.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sf.chatapp.remote.model.Content
import com.sf.chatapp.remote.model.Conversation
import com.sf.chatapp.remote.model.RequestBody
import com.sf.chatapp.utils.FirebaseConstraint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ChatBotRepository(
    private val geminiRepository: GeminiRepository
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val conversationRef = firestore.collection(FirebaseConstraint.USER)
        .document(firebaseAuth.currentUser!!.uid)
        .collection(FirebaseConstraint.CHATBOT)

    fun getConversationMapFlow() = callbackFlow<Map<String,Conversation>> {
        val listener = conversationRef.addSnapshotListener { value, error ->
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
                trySend(tempConversation)
            }

        }
        awaitClose {
            listener.remove()
        }
    }

    fun getConversation(key:String):DocumentReference{
         return conversationRef.document(key)
    }
    fun getNewConversation():DocumentReference{
        return conversationRef.document()
    }

    suspend fun sendMessage(
        currentConversation:DocumentReference,
        messageRequest:RequestBody
    ):List<Content>{
        val geminiResponse = geminiRepository.sendMessage(messageRequest)
        val message = messageRequest.contents + geminiResponse.candidates[0].content

        val conversation = Conversation(
            content = message
        )
        currentConversation.set(conversation).addOnFailureListener{
            throw Exception("chatbot repository error ${it.message}")
        }

        return message

    }

}