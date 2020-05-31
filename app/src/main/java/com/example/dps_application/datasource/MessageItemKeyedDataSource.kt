package com.example.dps_application.datasource

import android.util.Log
import androidx.annotation.NonNull
import androidx.paging.ItemKeyedDataSource
import androidx.room.InvalidationTracker
import com.example.dps_application.domain.executor.PostExecutionThread
import com.example.dps_application.domain.executor.ThreadExecutor
import com.example.dps_application.domain.model.User
import com.example.dps_application.domain.model.response.AttachmentResponse
import com.example.dps_application.domain.model.response.MessageResponse
import com.example.dps_application.domain.repository.MessageRepository
import com.example.dps_application.domain.repository.UserRepository
import com.example.dps_application.util.MessageKey
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

class MessageItemKeyedDataSource(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) : ItemKeyedDataSource<MessageKey, MessageResponse>() {

    private val disposables = CompositeDisposable()

    init {
        messageRepository.getInvalidationTracker().addObserver(
            object : InvalidationTracker.Observer("messages", "attachments") {
                override fun onInvalidated(@NonNull tables: Set<String>) {
                    invalidate()
                }
            })
    }

    override fun loadInitial(
        params: LoadInitialParams<MessageKey>,
        callback: LoadInitialCallback<MessageResponse>
    ) {
        val key = params.requestedInitialKey
        if(key != null) {
            getMessages(
                request = messageRepository.getMessagesBeforeFromDb(
                    key.chatId,
                    key.messageId,
                    params.requestedLoadSize
                ),
                reverse = true,
                callback = callback
            )
        } else {
            getMessages(
                request = messageRepository.getLastMessagesFromDb(
                    userRepository.getChatId(),
                    params.requestedLoadSize
                ),
                callback = callback
            )
        }
    }

    override fun loadAfter(params: LoadParams<MessageKey>, callback: LoadCallback<MessageResponse>) {
        getMessages(
            request = messageRepository.getMessagesAfterFromDb(
                params.key.chatId,
                params.key.messageId,
                params.requestedLoadSize
            ),
            callback = callback
        )
    }

    override fun loadBefore(params: LoadParams<MessageKey>, callback: LoadCallback<MessageResponse>) {
        getMessages(
            request = messageRepository.getMessagesBeforeFromDb(
                chatId = params.key.chatId,
                messageId = params.key.messageId,
                limit = params.requestedLoadSize
            ),
            reverse = true,
            callback = callback
        )
    }

    override fun getKey(item: MessageResponse)
            = MessageKey(item.chatId, item.messageId)

    private fun getMessages(request: Single<List<MessageResponse>>, reverse: Boolean = false, callback: LoadCallback<MessageResponse>) {
        disposables.add(
            request
                .flattenAsObservable { it }
                .flatMapSingle { message ->
                    Single.zip(
                        messageRepository.getUser(message.userId),
                        messageRepository.getAttachments(message.chatId, message.messageId),
                        BiFunction<User, List<AttachmentResponse>, MessageResponse> { user, attachments ->
                            message.user = user
                            message.attachments = attachments
                            message
                        }
                    )
                }
                .toList()
                .subscribe(
                    { messages ->
                        if(reverse)
                            messages.reverse()
                        callback.onResult(messages)
                    },
                    { e -> Log.d("1337", "e: $e")}
                )
        )
    }

    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}