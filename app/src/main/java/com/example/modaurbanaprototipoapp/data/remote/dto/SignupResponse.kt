package com.example.modaurbanaprototipoapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SignupResponse(
    val success: Boolean,
    val message: String?,
    val data: SignupData
)

data class SignupData(
    val user: UserDto,
    @SerializedName("access_token")
    val accessToken: String
)
