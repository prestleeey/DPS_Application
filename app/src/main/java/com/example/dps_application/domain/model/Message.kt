package com.example.dps_application.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import java.util.*

@Entity(
    tableName = "messages",
    primaryKeys = ["id", "chat_id"]
//    foreignKeys = [ForeignKey(
//        entity = User::class,
//        parentColumns = ["id"],
//        childColumns = ["user_id"],
//        onDelete = CASCADE)]
)
data class Message(
    @ColumnInfo(name = "id")
    var messageId: Int = 0,
    @ColumnInfo(name = "chat_id")
    var chatId: Int = 0,
    @ColumnInfo(name = "user_id")
    var userId: Int = 0,
    @ColumnInfo(name = "text")
    var text: String = "",
    @ColumnInfo(name = "date")
    var date: Date = Date()
)