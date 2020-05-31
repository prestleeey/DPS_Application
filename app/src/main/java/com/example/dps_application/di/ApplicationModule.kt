package com.example.dps_application.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.room.Room
import com.example.dps_application.api.*
import com.example.dps_application.db.DPSDb
import com.example.dps_application.domain.repository.TokenRepository
import com.example.dps_application.util.*
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, ExecutorModule::class, RepositoryModule::class])
class ApplicationModule {

    @Provides
    @Singleton
    fun provideDPSDatabase(application: Application) =
        Room.databaseBuilder(application, DPSDb::class.java, "dps.db")
            .fallbackToDestructiveMigration()
            .build()

    @ApiClient
    @Provides
    @Singleton
    fun provideUserAPIService(@ApiClient retrofit: Retrofit) = retrofit.create(UserAPI::class.java)

    @Provides
    @Singleton
    fun provideMessageAPIService(@ApiClient retrofit: Retrofit) = retrofit.create(MessageAPI::class.java)

    @ApiClient
    @Provides
    @Singleton
    fun provideRetrofitForApi(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(Constants.URL)
        .client(client)
        .addConverterFactory(WrapperConverterFactory(GsonConverterFactory.create(GsonBuilder().setExclusionStrategies(AnnotationExclusionStrategy()).create())))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()


    @RefreshTokenClient
    @Provides
    @Singleton
    fun provideUserAPIForRefreshToken() = Retrofit.Builder()
        .baseUrl(Constants.URL)
        .addConverterFactory(WrapperConverterFactory(GsonConverterFactory.create(GsonBuilder().setExclusionStrategies(AnnotationExclusionStrategy()).create())))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build())
        .build()
        .create(UserAPI::class.java)

    @Provides
    @Singleton
    fun provideClient(tokenRepository: TokenRepository, serverAuthenticator: ServerAuthenticator) =
        OkHttpClient.Builder()
            .authenticator(serverAuthenticator)
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request = chain.request()

                    val builder = request.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Connection", "close")

                    if(!tokenRepository.loadTokenFromLocal().isNull()) {
                        builder.addHeader(
                            "Authorization",
                            "Bearer " + tokenRepository.loadTokenFromLocal().accessToken
                        )
                    }

                    request = builder.build()

                    return chain.proceed(request)

                }
            })
            .build()

    @Provides
    @Singleton
    fun provideSPref(application: Application) =
        application.getSharedPreferences(Constants.NAME_SHAREDPREFERENCES, MODE_PRIVATE)

    @Provides
    @NETWORK_IO
    fun provideNetrworkIO(): Executor = Executors.newFixedThreadPool(5)

    @Provides
    @DISK_IO
    fun provideDiskIO(): Executor = Executors.newSingleThreadExecutor()

}