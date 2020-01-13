package com.example.dps_application.api

import com.google.gson.annotations.SerializedName

data class WrapperResponse<T> (
    @SerializedName("data")
    val data : T? = null,
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("error")
    val message: String = ""
)