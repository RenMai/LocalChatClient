package com.example.chatclientem

import android.util.Log
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.util.*
/**
 *  Starts a while loop to listen incoming messages from the server.
 *  Incoming messages are parsed as a ChatMessage and are passed to chatcontroller
 * */

class IncomingListener(private val chatAdapter: ChatRecyclerAdapter, private val usersAdapter:UserRecyclerAdapter, private val input: Scanner, private val activity: MainActivity ) : Runnable {
    override fun run() {
        try {
            while (true) {
                val message = Json.parse(ChatMessage.serializer(), input.nextLine())
                ChatConnector((message), chatAdapter,usersAdapter, activity).command()
                println("receive Message here")
            }
        } catch (e: Exception) {
            Log.d("moi", "$e :ei toimi kai")
        }
    }
}