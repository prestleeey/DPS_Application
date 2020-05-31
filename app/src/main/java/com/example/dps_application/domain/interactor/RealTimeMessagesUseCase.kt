package com.example.dps_application.domain.interactor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.dps_application.domain.executor.PostExecutionThread
import com.example.dps_application.domain.executor.ThreadExecutor
import com.example.dps_application.domain.interactor.base.UseCase
import com.example.dps_application.domain.model.request.MessageRequest
import com.example.dps_application.domain.model.response.MessageResponse
import com.example.dps_application.domain.repository.MessageRepository
import com.example.dps_application.domain.repository.TokenRepository
import com.example.dps_application.domain.repository.UserRepository
import com.example.dps_application.util.Constants
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import javax.inject.Inject

class RealTimeMessagesUseCase
@Inject constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
): UseCase<Unit, Unit> {

    private var socketId = ""
    private lateinit var socket: Socket

    override fun execute(params: Unit?): Unit {
        socket = IO.socket(Constants.SOCKET_URL)

        socket.on("App\\Events\\Chat\\NewMessage") {
            val message = Gson().fromJson(JSONObject(it.elementAtOrNull(1).toString()).get("message").toString(), MessageResponse::class.java)
            messageRepository.saveMessage(message)
                .subscribeOn(threadExecutor.scheduler)
                .observeOn(postExecutionThread.scheduler)
                .subscribe({}, { e -> Log.d("1337", "error: " + e.message) })
        }

        socket.on("connect") {
            socketId = socket.id()
        }

        socket.emit("subscribe", JSONObject("{ \"channel\": \"private-chat.${userRepository.getChatId()}\", \"auth\": {\"headers\": {\"Authorization\": \"Bearer ${tokenRepository.loadTokenFromLocal().accessToken}\"}}}"))

        socket.connect()
    }

    fun getSocketId() = socketId

    fun reconnect() {
        socket.close().connect()
    }

    override fun finish() {
        socket.disconnect()
    }

}