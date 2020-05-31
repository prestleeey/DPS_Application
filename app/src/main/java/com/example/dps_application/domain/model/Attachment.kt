package com.example.dps_application.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.dps_application.util.AttachmentTypes
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "attachments"
//    foreignKeys = [ForeignKey(
//        entity = Message::class,
//        parentColumns = ["id", "chat_id"],
//        childColumns = ["message_id", "message_chat_id"],
//        onDelete = CASCADE
//    )]
)
data class Attachment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "message_id")
    var messageId: Int = 0,
    @ColumnInfo(name = "message_chat_id")
    var chatId: Int = 0,
    @SerializedName("type")
    @ColumnInfo(name = "type")
    var type: String = AttachmentTypes.UNKNOWN.name,
    @SerializedName("url")
    @ColumnInfo(name = "url")
    var url: String = ""
    )