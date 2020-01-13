package com.example.dps_application.api

import com.google.gson.annotations.SerializedName
import java.lang.RuntimeException

data class WrapperError(
    @SerializedName("error")
    override val message: String = ""
) : RuntimeException()