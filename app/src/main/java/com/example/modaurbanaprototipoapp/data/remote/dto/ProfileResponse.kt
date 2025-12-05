package com.example.modaurbanaprototipoapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Respuesta completa del backend para /auth/profile
data class ProfileResponse(
    val success: Boolean,
    val data: ProfileDataDto
)

// "data" del JSON: contiene user + profile
data class ProfileDataDto(
    val user: UserDto,
    val profile: ProfileDto
)

// Info extra del perfil (nombre, preferencias, etc.)
data class ProfileDto(
    @SerializedName("_id")
    val id: String? = null,
    val nombre: String? = null,
    val preferencias: List<String>? = null,
    @SerializedName("isActive")
    val isActive: Boolean? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)
