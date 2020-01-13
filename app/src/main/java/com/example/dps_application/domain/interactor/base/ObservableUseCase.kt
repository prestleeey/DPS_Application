package com.example.dps_application.domain.interactor.base

import com.example.dps_application.util.empty_observer.EmptyObserver
import com.example.dps_application.domain.executor.PostExecutionThread
import com.example.dps_application.domain.executor.ThreadExecutor
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver


abstract class ObservableUseCase<Results, in Params>(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BaseReactiveUseCase(threadExecutor, postExecutionThread) {

    abstract fun buildUseCaseObservable(params: Params? = null): Observable<Results>

    fun execute(observer: DisposableObserver<Results> = EmptyObserver(), params: Params? = null) {
        val observable = buildUseCaseObservableWithSchedulers(params)
        addDisposable(observable.subscribeWith(observer))
    }

    private fun buildUseCaseObservableWithSchedulers(params: Params?): Observable<Results> {
        return buildUseCaseObservable(params)
                .subscribeOn(threadExecutorScheduler)
                .observeOn(postExecutionThreadScheduler)
    }
}
