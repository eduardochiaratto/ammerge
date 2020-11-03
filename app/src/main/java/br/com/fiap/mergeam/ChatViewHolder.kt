package br.com.fiap.mergeam

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val textViewUserMessage = view.findViewById<TextView>(R.id.textViewUserMessage)
    private val textViewBotMessage = view.findViewById<TextView>(R.id.textViewBotMessage)


    fun bind(message: Message) {
        if(message.uid != "") {
            textViewUserMessage.text = message.message
            textViewBotMessage.visibility = View.GONE
        } else {
            textViewBotMessage.text = message.message
            textViewUserMessage.visibility = View.GONE
        }
    }
}