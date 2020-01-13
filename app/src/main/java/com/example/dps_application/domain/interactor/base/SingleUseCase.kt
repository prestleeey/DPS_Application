package com.example.dps_application.domain.interactor.base

import com.example.dps_application.util.empty_observer.EmptySingleObserver
import com.example.dps_application.domain.executor.PostExecutionThread
import com.example.dps_application.domain.executor.ThreadExecutor
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver

abstract class SingleUseCase<Results, in Params>(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseReactiveUseCase(threadExecutor, postExecutionThread) {

    abstract fun buildUseCaseSingle(params: Params? = null): Single<Results>

    fun execute(observer: DisposableSingleObserver<Results> = EmptySingleObserver(), params: Params? = null) {
        val single = buildUseCaseSingleWithSchedulers(params)
        addDisposable(single.subscribeWith(observer))
    }

    private fun buildUseCaseSingleWithSchedulers(params: Params?): Single<Results> {
        return buildUseCaseSingle(params)
                .subscribeOn(threadExecutorScheduler)
                .observeOn(postExecutionThreadScheduler)
    }
}
