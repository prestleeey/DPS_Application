package com.example.dps_application.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.dps_application.api.ServerAuthenticator
import com.example.dps_application.api.UserAPI
import com.example.dps_application.api.WrapperConverterFactory
import com.example.dps_application.domain.repository.TokenRepository
import com.example.dps_application.util.ApiClient
import com.example.dps_application.util.RefreshTokenClient
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, ExecutorModule::class, RepositoryModule::class])
class ApplicationModule {

    private val URL = "http://192.168.1.5:88/api/"
    private val NAME_SHAREDPREFERENCES = "DPS_SPREF"

    @ApiClient
    @Provides
    @Singleton
    fun provideRetrofitForApi(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(URL)
        .client(client)
        .addConverterFactory(WrapperConverterFactory(GsonConverterFactory.create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(UserAPI::class.java)


    @RefreshTokenClient
    @Provides
    @Singleton
    fun provideRetrofitForRefreshToken() = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(WrapperConverterFactory(GsonConverterFactory.create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(UserAPI::class.java)

    @Provides
    @Singleton
    fun provideSPref(application : Application) =
        application.getSharedPreferences(NAME_SHAREDPREFERENCES, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideClient(tokenRepository: TokenRepository, serverAuthenticator: ServerAuthenticator) =
        OkHttpClient.Builder()
            .authenticator(serverAuthenticator)
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

}