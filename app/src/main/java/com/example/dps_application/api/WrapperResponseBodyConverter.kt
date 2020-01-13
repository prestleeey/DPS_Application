package com.example.dps_application.api

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Converter


class WrapperResponseBodyConverter<T>
constructor(
    private val converter: Converter<ResponseBody, WrapperResponse<T>>
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? {

        val response = converter.convert(value)

        if((response?.success ?: throw WrapperError("unknown error")))
            return response.data ?: throw WrapperError(response.message)
        else
            throw WrapperError(response.message)

    }

}