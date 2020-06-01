package com.example.dps_application.api

import com.example.dps_application.domain.repository.TokenRepository
import com.example.dps_application.domain.model.AccessToken
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class ServerAuthenticator
@Inject constructor(
    private val tokenRepository: TokenRepository
): Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        val outdatedToken = tokenRepository.loadTokenFromLocal()
        val newToken = tokenRepository.refreshToken(outdatedToken).blockingGet()

        return if(newToken is AccessToken)
             response.request.newBuilder()
                .header("Authorization", "Bearer " + newToken.refreshToken)
                .build()
        else
            null

    }

}