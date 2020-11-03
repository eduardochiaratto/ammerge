package br.com.fiap.mergeam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codepalace.chatbot.ui.ChatAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class ChatActivity : AppCompatActivity() {

    lateinit var editTextUserMessage: EditText
    lateinit var buttonSendMessage: Button

    lateinit var recyclerViewChat: RecyclerView
    val chatAdapter = ChatAdapter()

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        editTextUserMessage = findViewById(R.id.editTextUserMessage)
        buttonSendMessage = findViewById(R.id.buttonSendMessage)


        recyclerViewChat = findViewById(R.id.recyclerViewChat)
        recyclerViewChat.adapter = chatAdapter
        recyclerViewChat.layoutManager = LinearLayoutManager(applicationContext)

        auth = Firebase.auth


        val currentUser = auth.currentUser?.uid.toString()

        buttonSendMessage.setOnClickListener {
            val message = editTextUserMessage.text.toString()

            if (message.isNotEmpty()) {
                chatAdapter.addItem(Message(message, currentUser))
                editTextUserMessage.setText("")
                botResponse(message)
            }
        }

        editTextUserMessage.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    recyclerViewChat.scrollToPosition(chatAdapter.itemCount - 1)

                }
            }
        }

        firstBotMessage("Olá! Seja bem vindo(a) ao atendimento do MPSP.\n\nAntes de começarmos, por favor informe sua cidade:")

    }

    private fun botResponse(message: String) {
        GlobalScope.launch {
            delay(1000)

            withContext(Dispatchers.Main) {
                val response = BotResponse.basicResponses(message)
                chatAdapter.addItem(Message(response))
                recyclerViewChat.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }
    }

    private fun firstBotMessage(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                chatAdapter.addItem(Message(message))
            }
        }
    }
}
