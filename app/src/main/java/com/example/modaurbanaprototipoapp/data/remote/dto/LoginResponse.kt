package com.example.modaurbanaprototipoapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val data: LoginData
)

data class LoginData(
    val user: UserDtoBackend,
    @SerializedName("access_token") val accessToken: String
)

data class UserDtoBackend(
    @SerializedName("_id") val id: String,
    val email: String,
    val role: String,
    @SerializedName("createdAt") val createdAt: String?
)