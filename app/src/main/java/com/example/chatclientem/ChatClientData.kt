package com.example.chatclientem
/**
 * Singelton to hold users name. List of messges and users.
 * */
object ChatClientData {
    var name = ""
    val chatMessages: ArrayList<ChatMessage> = arrayListOf()
    val users: MutableSet<String> = mutableSetOf()

    fun newMessage (chatMessage: ChatMessage) {
        chatMessages.add(chatMessage)
    }

    fun addUser (user: String) {
        users.add(user)
    }
}