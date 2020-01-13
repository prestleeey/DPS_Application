package com.example.dps_application.repository

import android.content.SharedPreferences
import com.example.dps_application.api.UserAPI
import com.example.dps_application.domain.repository.TokenRepository
import com.example.dps_application.util.RefreshTokenClient
import com.example.dps_application.domain.model.AccessToken
import com.example.dps_application.util.UserFields
import javax.inject.Inject

class TokenRepositoryImpl
@Inject constructor(
    @RefreshTokenClient private val userAPI: UserAPI, private val sPref : SharedPreferences
): TokenRepository {

    override fun loadTokenFromLocal() =  AccessToken(
            sPref.getString(UserFields.TOKEN_TYPE.field, "") ?: "",
            sPref.getInt(UserFields.TOKEN_EXPIRE.field, 0),
            sPref.getString(UserFields.TOKEN_ACCESS.field, "") ?: "",
            sPref.getString(UserFields.TOKEN_REFRESH.field, "") ?: ""
        )

    override fun authorization(token : String) = userAPI.authorization(token)

    override fun refreshToken(token : AccessToken) = userAPI.refreshToken(token.refreshToken)

    override fun saveToken(token: AccessToken) {
        val ed = sPref.edit()
        ed.putString(UserFields.TOKEN_TYPE.field, token.tokenType)
        ed.putInt(UserFields.TOKEN_EXPIRE.field, token.expiresIn)
        ed.putString(UserFields.TOKEN_ACCESS.field, token.accessToken)
        ed.putString(UserFields.TOKEN_REFRESH.field, token.refreshToken)
        ed.apply()
    }

}
