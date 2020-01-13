package com.example.dps_application.executor

import com.example.dps_application.domain.executor.ThreadExecutor
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobExecutor
@Inject
constructor() : ThreadExecutor {
    override val scheduler: Scheduler
        get() = Schedulers.io()
}