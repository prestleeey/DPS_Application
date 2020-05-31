package com.example.dps_application.domain.entities

import androidx.lifecycle.LiveData
import com.example.dps_application.domain.model.response.MessageResponse
import io.socket.client.Socket

data class RealTimeMessaging(
    val send: (text: String) -> Unit,
    val sendingMessagingEvent: LiveData<MessageResponse>,
    val reconnect: () -> Unit
)