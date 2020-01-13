package com.example.dps_application.domain.repository

import com.example.dps_application.domain.model.User
import io.reactivex.Single

interface UserRepository {
    fun saveUserInfo(user: User)
    fun getUserInfo(): Single<User>?
}