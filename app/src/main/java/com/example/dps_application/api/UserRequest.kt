package com.example.dps_application.api

import com.example.dps_application.domain.model.User
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class UserRequest : VKRequest<User>("users.get") {

    init {
        addParam("fields", "photo_200")
    }

    override fun parse(r: JSONObject): User {
        val users = r.getJSONArray("response")
        return User.parse(users.getJSONObject(0))
    }
}