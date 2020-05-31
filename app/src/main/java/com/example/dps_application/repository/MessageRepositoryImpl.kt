package com.example.dps_application.repository

import com.example.dps_application.api.MessageAPI
import com.example.dps_application.db.DPSDb
import com.example.dps_application.domain.model.request.MessageRequest
import com.example.dps_application.domain.model.response.MessageResponse
import com.example.dps_application.domain.repository.MessageRepository
import io.reactivex.Single
import javax.inject.Inject

class MessageRepositoryImpl
@Inject constructor(
    private val db: DPSDb,
    private val messageAPI: MessageAPI
) : MessageRepository {

    private fun getMessages(chatId: Int, messageId: Int? = null, type: String? = null, limit: Int? = null)
            = messageAPI.getMessages(chatId, messageId, type, limit)

    override fun getLastMessagesFromNetwork(chatId: Int, messageId: Int?)
            = getMessages(chatId, messageId)

    override fun getMessagesBeforeFromNetwork(chatId: Int, messageId: Int, limit: Int)
            = getMessages(chatId, messageId, RequestType.NEW.name, limit)

    override fun getMessagesAfterFromNetwork(chatId: Int, messageId: Int, limit: Int)
            = getMessages(chatId, messageId, RequestType.OLD.name, limit)

    override fun getLastMessagesFromDb(chatId: Int, limit: Int)
            = db.messages().getLastMessages(chatId, limit)

    override fun getMessagesBeforeFromDb(chatId: Int, messageId: Int, limit: Int)
            = db.messages().getMessagesBefore(chatId, messageId, limit)

    override fun getMessagesAfterFromDb(chatId: Int, messageId: Int, limit: Int)
            = db.messages().getMessagesAfter(chatId, messageId, limit)

    override fun getLastMessageId(chatId: Int)
            = db.messages().getLastMessageId(chatId)

    override fun getUser(userId: Int)
            = db.messages().getUser(userId)

    override fun getAttachments(chatId: Int, messageId: Int)
            = db.messages().getAttachments(chatId, messageId)

    override fun saveMessages(messages: List<MessageResponse>)
            = Single.fromCallable { db.messages().insertMessagesJSON(messages) }

    override fun saveMessage(message: MessageResponse)
            = Single.fromCallable { db.messages().insertMessagesJSON(listOf(message)) }

    override fun sendMessage(socketId: String, message: MessageRequest)
            = messageAPI.sendMessage(socketId, message)

    override fun deleteMessagesByIdBelow(chatId: Int, messageId: Int)
            = Single.fromCallable { db.messages().deleteMessagesByIdBelow(chatId, messageId) }

    override fun getInvalidationTracker() = db.invalidationTracker

    enum class RequestType {
        OLD,
        NEW
    }
}