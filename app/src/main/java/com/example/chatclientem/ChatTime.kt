package com.example.chatclientem

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object ChatTime {
    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    fun getChatTime():String
    {
        return LocalDateTime.now().format(formatter)
    }
}