package com.example.dps_application.domain.interactor.base

import com.example.dps_application.domain.executor.PostExecutionThread
import com.example.dps_application.domain.executor.ThreadExecutor
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseReactiveUseCase(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) {

    protected val threadExecutorScheduler: Scheduler = threadExecutor.scheduler

    protected val postExecutionThreadScheduler: Scheduler = postExecutionThread.scheduler

    private val disposables = CompositeDisposable()

    open fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    protected fun addDisposable(disposable: Disposable) {
        disposables.add(checkNotNull(disposable, { "disposable cannot be null!" }))
    }
}
