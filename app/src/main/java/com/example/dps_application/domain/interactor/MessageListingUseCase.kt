package com.example.dps_application.domain.interactor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.dps_application.datasource.MessageBoundaryCallback
import com.example.dps_application.datasource.MessageDataSourceFactory
import com.example.dps_application.domain.entities.Listing
import com.example.dps_application.domain.executor.PostExecutionThread
import com.example.dps_application.domain.executor.ThreadExecutor
import com.example.dps_application.domain.interactor.base.UseCase
import com.example.dps_application.domain.model.response.MessageResponse
import com.example.dps_application.domain.repository.MessageRepository
import com.example.dps_application.domain.repository.TokenRepository
import com.example.dps_application.domain.repository.UserRepository
import com.example.dps_application.util.DISK_IO
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executor
import javax.inject.Inject

class MessageListingUseCase
@Inject constructor(
    messageRepository: MessageRepository,
    userRepository: UserRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    @DISK_IO diskIo: Executor
) : UseCase<LiveData<PagedList<MessageResponse>>, Unit> {

    private val sourceFactory =
    MessageDataSourceFactory(messageRepository, userRepository, threadExecutor, postExecutionThread)

    private val boundaryCallback = MessageBoundaryCallback(
        messageRepository, userRepository, threadExecutor, postExecutionThread, diskIo
    )

    override fun execute(params: Unit?): LiveData<PagedList<MessageResponse>> {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(INITIAL_PAGE_SIZE)
            .setPrefetchDistance(DISTANCE)
            .build()

        return sourceFactory.toLiveData(
                config = config,
                boundaryCallback = boundaryCallback
        )

    }

    fun getNetworkState() = boundaryCallback.networkState

    fun retry() = boundaryCallback.helper.retryAllFailed()

    fun refresh() = boundaryCallback.initialLoad()

    override fun finish() {
        sourceFactory.sourceLiveData.value?.dispose()
        boundaryCallback.dispose()
    }

    companion object {
        const val PAGE_SIZE = 30
        const val INITIAL_PAGE_SIZE = 60
        const val DISTANCE = 20
    }
}