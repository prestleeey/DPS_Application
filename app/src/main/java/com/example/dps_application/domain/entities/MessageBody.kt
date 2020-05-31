package com.example.dps_application.domain.entities

data class MessageBody(
    val socketId: String,
    val text: String = ""
)