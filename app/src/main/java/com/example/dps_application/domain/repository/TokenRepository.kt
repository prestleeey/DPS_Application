package com.example.dps_application.domain.repository

import com.example.dps_application.domain.model.AccessToken
import com.example.dps_application.domain.model.response.AuthorizationResponse
import io.reactivex.Single

interface TokenRepository {
    fun loadTokenFromLocal(): AccessToken
    fun authorization(token: String): Single<AuthorizationResponse>
    fun refreshToken(token: AccessToken): Single<AccessToken>
    fun saveToken(token: AccessToken)
}