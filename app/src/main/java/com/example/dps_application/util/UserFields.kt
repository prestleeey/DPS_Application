package com.example.dps_application.util

enum class UserFields(var field : String) {
    FIRSTNAME("first_name"),
    LASTNAME("last_name"),
    PHOTO("photo"),
    ID("id"),
    TOKEN_TYPE("token_type"),
    TOKEN_EXPIRE("expires_in"),
    TOKEN_ACCESS("access_token"),
    TOKEN_REFRESH("refresh_token")
}