package com.codepalace.chatbot.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.mergeam.ChatViewHolder
import br.com.fiap.mergeam.Message
import br.com.fiap.mergeam.R
import kotlinx.android.synthetic.main.chat_item.view.*

class ChatAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var messagesList = mutableListOf<Message>()

    fun addItem(message: Message) {
        messagesList.add(message)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false))
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messagesList[position]

        val chatViewHolder = holder as ChatViewHolder
        chatViewHolder.bind(currentMessage)

    }

}