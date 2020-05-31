package com.example.dps_application.datasource

import android.util.Log
import androidx.paging.PagedList
import com.example.dps_application.domain.executor.PostExecutionThread
import com.example.dps_application.domain.executor.ThreadExecutor
import com.example.dps_application.domain.interactor.MessageListingUseCase
import com.example.dps_application.domain.model.response.MessageResponse
import com.example.dps_application.domain.repository.MessageRepository
import com.example.dps_application.domain.repository.UserRepository
import com.example.dps_application.util.PagingRequestHelper
import com.example.dps_application.util.createStatusLiveData
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import java.util.concurrent.Executor

class MessageBoundaryCallback (
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread,
    diskIo: Executor
    ) : PagedList.BoundaryCallback<MessageResponse>() {

    val helper = PagingRequestHelper(diskIo)
    private val disposables = CompositeDisposable()
    val networkState = helper.createStatusLiveData()

//    override fun onZeroItemsLoaded() {
//        Log.d("1337", "onZeroItemsLoaded");
//        initialLoad()
//    }

    override fun onItemAtEndLoaded(itemAtEnd: MessageResponse) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helper ->
            disposables.add(
                messageRepository.getMessagesAfterFromNetwork(
                    chatId = itemAtEnd.chatId,
                    messageId = itemAtEnd.messageId,
                    limit = MessageListingUseCase.PAGE_SIZE
                )
                    .flatMap { messages ->
                        messageRepository.saveMessages(messages)
                    }
                    .subscribeOn(threadExecutor.scheduler)
                    .observeOn(postExecutionThread.scheduler)
                    .subscribe(
                        { helper.recordSuccess() },
                        { e ->
                            Log.d("1337", "onItemAtEndLoaded error: $e")
                            helper.recordFailure(e)
                        }
                    )
            )
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: MessageResponse) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.BEFORE) { helper ->
            disposables.add(
                messageRepository.getMessagesBeforeFromNetwork(
                    chatId = itemAtFront.chatId,
                    messageId = itemAtFront.messageId,
                    limit = MessageListingUseCase.PAGE_SIZE
                )
                    .flatMap { messages ->
                        messageRepository.saveMessages(messages)
                    }
                    .subscribeOn(threadExecutor.scheduler)
                    .observeOn(postExecutionThread.scheduler)
                    .subscribe(
                        {
                            helper.recordSuccess()
                        },
                        { e ->
                            Log.d("1337", "onItemAtFrontLoaded error: $e")
                            helper.recordFailure(e)
                        }
                    )
                )
        }
    }

    fun initialLoad() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.BEFORE) { helper ->
            disposables.add(
                messageRepository.getLastMessageId(chatId = userRepository.getChatId())
                    .switchIfEmpty (Maybe.just(MessageResponse.ABSENT))
                    .toSingle()
                    .flatMap{ messageId ->
                        if(messageId == MessageResponse.ABSENT) {
                            messageRepository.getLastMessagesFromNetwork(
                                chatId = userRepository.getChatId(),
                                messageId = null
                            )
                                .retry(2)
                        } else {
                            messageRepository.getLastMessagesFromNetwork(
                                chatId = userRepository.getChatId(),
                                messageId = messageId
                            )
                                .retry(2)
                        }
                    }
                    .flatMap { messages ->
                        if (messages.size == 1000) {
                            messageRepository.saveMessages(messages).zipWith(
                                messageRepository.deleteMessagesByIdBelow(
                                    messages[999].chatId,
                                    messages[999].messageId
                                ),
                                BiFunction { _, _ -> Unit }
                            )
                        } else {
                            messageRepository.saveMessages(messages)
                        }
                    }
                    .subscribeOn(threadExecutor.scheduler)
                    .observeOn(postExecutionThread.scheduler)
                    .subscribe(
                        {
                            helper.recordSuccess()
                        },
                        { e ->
                            helper.recordFailure(e)
                            Log.d("1337", "Get top message error: " + e.message)
                        }
                    )
            )
        }
    }

    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

}