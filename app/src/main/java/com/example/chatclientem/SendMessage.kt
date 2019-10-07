package com.example.chatclientem

import android.util.Log
import kotlinx.serialization.json.Json
import java.io.PrintWriter
import java.lang.Exception

/**
 *  Takes a ChatMessage and sends it to the server in a json string.
 * */
class SendMessage(private val chatMessage: ChatMessage, private val output: PrintWriter): Runnable{
    override fun run() {
        try {
            output.println(Json.stringify(ChatMessage.serializer(),chatMessage))
        } catch (e: Exception) {
            Log.d("moi", "$e :ei toimi kai")
        }
    }
}