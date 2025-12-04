package com.example.modaurbanaprototipoapp.data.remote.dto

data class SignupRequest(
    val email: String,
    val password: String,
    val nombre: String,
    val role: String = "CLIENTE"
)
