package com.example.dps_application.domain.model

import com.google.gson.annotations.SerializedName

data class AccessToken(
    @SerializedName("token_type")
    var tokenType: String = "",
    @SerializedName("expires_in")
    var expiresIn: Int = 0,
    @SerializedName("access_token")
    var accessToken: String = "",
    @SerializedName("refresh_token")
    var refreshToken: String = ""
    ) {

    fun isNull() = refreshToken == "" && accessToken == ""

}