package com.example.dps_application.di

import com.example.dps_application.domain.repository.MessageRepository
import com.example.dps_application.domain.repository.TokenRepository
import com.example.dps_application.domain.repository.UserRepository
import com.example.dps_application.repository.MessageRepositoryImpl
import com.example.dps_application.repository.TokenRepositoryImpl
import com.example.dps_application.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindTokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindMessageRepository(messageRepositoryImpl: MessageRepositoryImpl): MessageRepository

}