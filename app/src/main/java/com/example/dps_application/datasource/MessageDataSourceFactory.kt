package com.example.dps_application.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.dps_application.domain.executor.PostExecutionThread
import com.example.dps_application.domain.executor.ThreadExecutor
import com.example.dps_application.domain.model.response.MessageResponse
import com.example.dps_application.domain.repository.MessageRepository
import com.example.dps_application.domain.repository.UserRepository
import com.example.dps_application.util.MessageKey

class MessageDataSourceFactory(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) : DataSource.Factory<MessageKey, MessageResponse>() {
    val sourceLiveData = MutableLiveData<MessageItemKeyedDataSource>()
    override fun create(): DataSource<MessageKey, MessageResponse> {
        val source =  MessageItemKeyedDataSource(
            messageRepository,
            userRepository,
            threadExecutor,
            postExecutionThread
        )
        sourceLiveData.postValue(source)
        return source
    }
}