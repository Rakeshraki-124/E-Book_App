package com.example.e_book.chatbot

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messsageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = Constants.api_key
    )


    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = messsageList.map {
                        content(it.role){text(it.message)}
                    }.toList()
                )

                messsageList.add(MessageModel(question, "user"))
                messsageList.add(MessageModel("Typing...", "model"))

                val response = chat.sendMessage(question)
                messsageList.removeLast()
                messsageList.add(MessageModel(response.text.toString(), "model"))

                Log.d("Response from Gemini", response.text.toString())
            } catch (e: Exception) {
                messsageList.removeLast()
                messsageList.add(MessageModel("Error: ${e.message.toString()}", "model"))
                Log.e("Chat Error", "Error sending message: ${e.message}")
            }
        }
    }
}