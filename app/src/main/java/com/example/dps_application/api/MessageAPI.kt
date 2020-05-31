package com.example.dps_application.api

import com.example.dps_application.domain.model.request.MessageRequest
import com.example.dps_application.domain.model.response.MessageResponse
import io.reactivex.Single
import retrofit2.http.*

interface MessageAPI {
    @GET("chat/messages")
    fun getMessages(
        @Query("chat_id") chatId: Int,
        @Query("id") messageId: Int?,
        @Query("type") type: String?,
        @Query("limit") limit: Int?
    ): Single<List<MessageResponse>>

    @POST("chat/messages")
    fun sendMessage(@Header("X-Socket-ID") socketId: String, @Body body: MessageRequest): Single<MessageResponse>
}