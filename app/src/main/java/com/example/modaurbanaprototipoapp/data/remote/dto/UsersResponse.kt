package com.example.modaurbanaprototipoapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("users")
    val users: List<UserDto>,

    @SerializedName("total")
    val total: Int,

    @SerializedName("skip")
    val skip: Int,

    @SerializedName("limit")
    val limit: Int
)