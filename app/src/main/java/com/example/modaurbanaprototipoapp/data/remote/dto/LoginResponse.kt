package com.example.modaurbanaprototipoapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String?
)