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
        ed.putString(UserFields.FIRSTNAME.name, user.firstName)
        ed.putString(UserFields.LASTNAME.name, user.lastName)
        ed.putString(UserFields.PHOTO.name, user.photo)
        ed.putInt(UserFields.ID.name, user.id)
        ed.putInt(UserFields.VK_ID.name, user.vkId)
        ed.apply()
    }

    override fun getUserInfoFromBase(): User =
         User(
             sPref.getInt(UserFields.ID.name, 0),
             sPref.getInt(UserFields.VK_ID.name, 0),
             sPref.getString(UserFields.FIRSTNAME.name, "") ?: "",
             sPref.getString(UserFields.LASTNAME.name, "") ?: "",
             sPref.getString(UserFields.PHOTO.name, "") ?: "",
             true
         )

    override fun saveChatId(chatId: Int) {
        val ed = sPref.edit()
        ed.putInt(UserFields.CHAT_ID.name, chatId)
        ed.apply()
    }

    override fun getChatId() = sPref.getInt(UserFields.CHAT_ID.name, 1)
}