package com.example.dps_application.api

import com.example.dps_application.domain.model.AccessToken
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface UserAPI {

    @GET("auth/login")
    fun authorization(@Query("vk_token") token: String): Single<AccessToken>

    @GET("auth/refreshToken")
    fun refreshToken(@Query("refresh_token") token: String): Single<AccessToken>

}