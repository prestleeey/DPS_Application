package com.example.dps_application.domain.interactor.base

interface SynchronousUseCase<out Results, in Params> {

    fun execute(params: Params? = null): Results
}
