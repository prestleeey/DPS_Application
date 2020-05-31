package com.example.dps_application.db

import androidx.room.*
import com.example.dps_application.domain.model.Attachment
import com.example.dps_application.domain.model.Message
import com.example.dps_application.domain.model.User
import com.example.dps_application.domain.model.response.AttachmentResponse
import com.example.dps_application.domain.model.response.MessageResponse
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAttachments(attachments: List<Attachment>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Transaction
    fun insertMessagesJSON(messagesResponse: List<MessageResponse>) {
        for (messageJSON in messagesResponse) {
            insertUser(messageJSON.user)
            insertMessage(messageJSON.toMessage())
            if(messageJSON.attachments.isNotEmpty())
                insertAttachments(messageJSON.toAttachment())
        }
    }

    @Query("SELECT * FROM messages WHERE chat_id = :chatId ORDER BY id DESC LIMIT :limit")
    fun getLastMessages(chatId: Int, limit: Int): Single<List<MessageResponse>>

    @Query("SELECT * FROM messages WHERE chat_id = :chatId AND id > :messageId ORDER BY id ASC LIMIT :limit")
    fun getMessagesBefore(chatId: Int, messageId: Int, limit: Int): Single<List<MessageResponse>>

    @Query("SELECT * FROM messages WHERE chat_id = :chatId AND id < :messageId ORDER BY id DESC LIMIT :limit")
    fun getMessagesAfter(chatId: Int, messageId: Int, limit: Int): Single<List<MessageResponse>>

    @Query("SELECT type, url FROM attachments WHERE message_id = :messageId AND message_chat_id = :chatId ORDER BY id DESC")
    fun getAttachments(chatId: Int, messageId: Int): Single<List<AttachmentResponse>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: Int): Single<User>

    @Query("DELETE FROM messages WHERE chat_id = :chatId AND id < :messageId")
    fun deleteMessagesByIdBelow(chatId: Int, messageId: Int)

    @Query("SELECT id FROM messages WHERE chat_id = :chatId ORDER BY id DESC LIMIT 1")
    fun getLastMessageId(chatId: Int) : Maybe<Int?>
}