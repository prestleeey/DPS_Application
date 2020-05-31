package com.example.dps_application.domain.model.response

import com.example.dps_application.domain.model.AccessToken
import com.google.gson.annotations.SerializedName

data class AuthorizationResponse(
    @SerializedName("id")
    val userId: Int,
    @SerializedName("token")
    val token: AccessToken
)