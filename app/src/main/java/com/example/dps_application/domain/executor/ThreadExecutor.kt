package com.example.dps_application.domain.executor

import io.reactivex.Scheduler

interface ThreadExecutor {
    val scheduler: Scheduler
}
