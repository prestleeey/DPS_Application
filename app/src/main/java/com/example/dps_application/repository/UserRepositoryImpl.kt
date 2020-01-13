package com.example.dps_application.repository

import android.content.SharedPreferences
import com.example.dps_application.api.UserAPI
import com.example.dps_application.api.UserRequest
import com.example.dps_application.domain.repository.UserRepository
import com.example.dps_application.domain.model.User
import com.example.dps_application.util.ApiClient
import com.example.dps_application.util.UserFields
import com.vk.api.sdk.VK
import io.reactivex.Single
import javax.inject.Inject

class UserRepositoryImpl
@Inject constructor(
    @ApiClient private val userAPI: UserAPI,
    private val sPref : SharedPreferences
) : UserRepository {

    override fun getUserInfo() =  Single.fromCallable { VK.executeSync(UserRequest()) }

    override fun saveUserInfo(user: User) {
        val ed = sPref.edit()
        ed.putString(UserFields.FIRSTNAME.field, user.firstName)
        ed.putString(UserFields.LASTNAME.field, user.lastName)
        ed.putString(UserFields.PHOTO.field, user.photo)
        ed.putInt(UserFields.ID.field, user.id)
        ed.apply()
    }

}