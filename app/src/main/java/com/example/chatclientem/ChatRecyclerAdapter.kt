package com.example.chatclientem

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_chat_message.view.*

/**
 * Adapter for chatview recycler with clicklistener on usernames and also changes the username color if the incoming message is private
 * */

class ChatRecyclerAdapter(private val context: Activity, private val receiver: TextView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_chat_message, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ChatViewHolder -> {
                holder.bind(ChatClientData.chatMessages[position])
                holder.itemView.username.setOnClickListener{
                    // toast on clicked username
                    val toast = Toast.makeText(context ,"private message to: "  +holder.itemView.username.text, Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0 , 0)
                    toast.show()
                    // sets the receiver based on clicked name
                    receiver.text = holder.itemView.username.text
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return ChatClientData.chatMessages.size
    }

    class ChatViewHolder(chatView: View): RecyclerView.ViewHolder(chatView) {
        private val username = chatView.username
        private val message = chatView.userMessage

        fun bind(chatMessage: ChatMessage){
            username.text = chatMessage.name()
            if (username.text == "Me")
            {
                
            }
            message.text = chatMessage.toString()
            // if incoming chanmessages command is whisper change the username color

        }
    }



}