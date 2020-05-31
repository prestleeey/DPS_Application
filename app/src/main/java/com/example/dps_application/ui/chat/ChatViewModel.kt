package com.example.dps_application.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dps_application.domain.entities.MessageBody
import com.example.dps_application.domain.interactor.MessageListingUseCase
import com.example.dps_application.domain.interactor.RealTimeMessagesUseCase
import com.example.dps_application.domain.interactor.SendMessageUseCase
import com.example.dps_application.domain.model.response.MessageResponse
import com.example.dps_application.util.SingleLiveEvent
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class ChatViewModel
@Inject constructor(
    private val messageListingUseCase: MessageListingUseCase,
    private val realTimeMessagesUseCase: RealTimeMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val sendMessageEvent = SingleLiveEvent<Unit>()

    init {
        realTimeMessagesUseCase.execute()
    }

    val messages = messageListingUseCase.execute()
    val networkState = messageListingUseCase.getNetworkState()

    fun refresh() {
        messageListingUseCase.refresh()
    }

    fun retry() {
        messageListingUseCase.retry()
        realTimeMessagesUseCase.reconnect()
    }

    fun sendMessage(text: String) {
        sendMessageEvent.call()
        sendMessageUseCase.execute(object : DisposableSingleObserver<MessageResponse>() {
            override fun onSuccess(t: MessageResponse) {
                Log.d("1337", "t: "+t);
            }

            override fun onError(e: Throwable) {
                Log.d("1337", "e: "+e);
            }
        }, MessageBody(realTimeMessagesUseCase.getSocketId(), text))
    }

    fun sendMessageEvent() = sendMessageEvent


    override fun onCleared() {
        realTimeMessagesUseCase.finish()
        messageListingUseCase.finish()
        sendMessageUseCase.dispose()
        super.onCleared()
    }
}