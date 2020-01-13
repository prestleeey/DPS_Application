package com.example.dps_application.di

import com.example.dps_application.domain.executor.PostExecutionThread
import com.example.dps_application.domain.executor.ThreadExecutor
import com.example.dps_application.executor.JobExecutor
import com.example.dps_application.executor.UIThread
import dagger.Binds
import dagger.Module

@Module
abstract class ExecutorModule {

    @Binds
    abstract fun bindThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor

    @Binds
    abstract fun bindPostExecutionThread(uiThread: UIThread): PostExecutionThread

}