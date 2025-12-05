package com.example.modaurbanaprototipoapp.repository

import android.content.Context
import com.example.modaurbanaprototipoapp.data.remote.ApiService
import com.example.modaurbanaprototipoapp.data.remote.RetrofitClient
import com.example.modaurbanaprototipoapp.data.remote.dto.LoginRequest
import com.example.modaurbanaprototipoapp.data.remote.dto.LoginResponse
import com.example.modaurbanaprototipoapp.data.remote.dto.SignupRequest
import com.example.modaurbanaprototipoapp.data.remote.dto.SignupResponse
import com.example.modaurbanaprototipoapp.data.remote.dto.UserDto

class UserRepository(context: Context) {

    private val apiService: ApiService = RetrofitClient
        .create(context)
        .create(ApiService::class.java)

    // LOGIN
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email = email, password = password)
            val response = apiService.login(request)

            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.message ?: "Error al iniciar sesión"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // REGISTRO
    suspend fun signup(email: String, password: String, name: String): Result<SignupResponse> {
        return try {
            val request = SignupRequest(
                email = email,
                password = password,
                nombre = name,
                role = "CLIENTE"
            )

            val response = apiService.register(request)

            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.message ?: "Error al registrar usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // PERFIL ACTUAL
    suspend fun getCurrentUser(): Result<UserDto> {
        return try {
            val response = apiService.getCurrentUser()

            if (response.success) {
                // 👇 AHORA TOMAMOS data.user, NO data DIRECTO
                val user = response.data.user
                Result.success(user)
            } else {
                Result.failure(Exception("Error al obtener perfil de usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
