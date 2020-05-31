package com.example.dps_application.domain.model.request

import com.example.dps_application.domain.model.response.AttachmentResponse
import com.google.gson.annotations.SerializedName

data class MessageRequest (
    @SerializedName("chat_id")
    val chatId: Int = 0,
    @SerializedName("from_id")
    val userId: Int = 0,
    @SerializedName("text")
    val text: String = "",
    @SerializedName("attachments")
    val attachment: List<AttachmentResponse> = listOf()
)