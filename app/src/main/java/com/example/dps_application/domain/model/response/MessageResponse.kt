package com.example.dps_application.domain.model.response

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.example.dps_application.api.Exclude
import com.example.dps_application.domain.model.Attachment
import com.example.dps_application.domain.model.Message
import com.example.dps_application.domain.model.User
import com.google.gson.annotations.SerializedName
import java.util.*

data class MessageResponse(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var messageId: Int = 0,
    @SerializedName("chat_id")
    @ColumnInfo(name = "chat_id")
    var chatId: Int = 0,
    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    var userId: Int = 0,
    @SerializedName("text")
    @ColumnInfo(name = "text")
    var text: String = "",
    @Exclude
    @ColumnInfo(name = "date")
    var date: Date = Date(),
    @SerializedName("user")
    @Ignore
    var user: User = User(),
    @SerializedName("attachments")
    @Ignore
    var attachments: List<AttachmentResponse> = listOf()
) {

    fun toMessage() =
        Message(messageId, chatId, user.id, text, date)

    fun toAttachment() =
        attachments.map { Attachment(messageId = messageId, chatId = chatId, type = it.type, url = it.url) }

    override fun toString(): String {
        return toMessage().toString() + " " + user.toString() + " " + toAttachment().toString()
    }

    companion object {
        const val ABSENT = -1
    }
}