package com.example.modaurbanaprototipoapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("_id")
    val id: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("avatar")
    val avatar: String? = null,

    @SerializedName("isActive")
    val isActive: Boolean? = null,

    @SerializedName("emailVerified")
    val emailVerified: Boolean? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
)
