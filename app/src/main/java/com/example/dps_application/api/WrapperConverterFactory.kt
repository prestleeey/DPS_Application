package com.example.dps_application.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Singleton


@Singleton
class WrapperConverterFactory(private val factory: GsonConverterFactory) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {

        val wrappedType = object : ParameterizedType {
            override fun getActualTypeArguments(): Array<Type> {
                return arrayOf(type)
            }

            override fun getOwnerType(): Type? {
                return null
            }

            override fun getRawType(): Type {
                return WrapperResponse::class.java
            }
        }

        val gsonConverter: Converter<ResponseBody, *>? = factory.responseBodyConverter(wrappedType, annotations, retrofit)
        return WrapperResponseBodyConverter(gsonConverter as Converter<ResponseBody, WrapperResponse<Any>>)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return factory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }
}