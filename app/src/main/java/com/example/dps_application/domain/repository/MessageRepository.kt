package com.example.dps_application.domain.repository

import androidx.room.InvalidationTracker
import com.example.dps_application.domain.model.User
import com.example.dps_application.domain.model.response.AttachmentResponse
import com.example.dps_application.domain.model.request.MessageRequest
import com.example.dps_application.domain.model.response.MessageResponse
import io.reactivex.Maybe
import io.reactivex.Single

interface MessageRepository {
    fun getLastMessagesFromNetwork(chatId: Int, messageId: Int?) : Single<List<MessageResponse>>
    fun getMessagesBeforeFromNetwork(chatId: Int, messageId: Int, limit: Int) : Single<List<MessageResponse>>
    fun getMessagesAfterFromNetwork(chatId: Int, messageId: Int, limit: Int) : Single<List<MessageResponse>>
    fun getLastMessagesFromDb(chatId: Int, limit: Int) : Single<List<MessageResponse>>
    fun getMessagesBeforeFromDb(chatId: Int, messageId: Int, limit: Int) : Single<List<MessageResponse>>
    fun getMessagesAfterFromDb(chatId: Int, messageId: Int, limit: Int) : Single<List<MessageResponse>>
    fun getLastMessageId(chatId: Int) : Maybe<Int?>
    fun getUser(userId: Int) : Single<User>
    fun getAttachments(chatId: Int, messageId: Int) : Single<List<AttachmentResponse>>
    fun saveMessages(messages: List<MessageResponse>) : Single<Unit>
    fun saveMessage(message: MessageResponse) : Single<Unit>
    fun sendMessage(socketId: String, message: MessageRequest) : Single<MessageResponse>
    fun deleteMessagesByIdBelow(chatId: Int, messageId: Int) : Single<Unit>
    fun getInvalidationTracker() : InvalidationTracker
}