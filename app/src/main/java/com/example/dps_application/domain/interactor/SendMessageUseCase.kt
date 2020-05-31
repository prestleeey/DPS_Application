package com.example.dps_application.domain.interactor

import com.example.dps_application.domain.entities.MessageBody
import com.example.dps_application.domain.executor.PostExecutionThread
import com.example.dps_application.domain.executor.ThreadExecutor
import com.example.dps_application.domain.interactor.base.SingleUseCase
import com.example.dps_application.domain.model.request.MessageRequest
import com.example.dps_application.domain.model.response.MessageResponse
import com.example.dps_application.domain.repository.MessageRepository
import com.example.dps_application.domain.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

class SendMessageUseCase
@Inject constructor(
    private val messageRepository: MessageRepository,
    userRepository: UserRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
): SingleUseCase<MessageResponse, MessageBody>(threadExecutor, postExecutionThread) {

    private val userId = userRepository.getUserInfoFromBase().id
    private val chatId = userRepository.getChatId()

    override fun buildUseCaseSingle(params: MessageBody?): Single<MessageResponse> =
        messageRepository.sendMessage(
            params?.socketId ?: "",
            MessageRequest(chatId, userId, params?.text ?: "")
        )

}