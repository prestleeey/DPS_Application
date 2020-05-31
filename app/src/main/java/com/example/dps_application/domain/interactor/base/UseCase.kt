package com.example.dps_application.domain.interactor.base

interface UseCase<out Results, in Params> {

    fun execute(params: Params? = null): Results
    fun finish()
}
