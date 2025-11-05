package com.example.modaurbanaprototipoapp.repository

import android.content.Context
import com.example.modaurbanaprototipoapp.data.remote.ApiService
import com.example.modaurbanaprototipoapp.data.remote.RetrofitClient
import com.example.modaurbanaprototipoapp.data.remote.dto.LoginRequest
import com.example.modaurbanaprototipoapp.data.remote.dto.LoginResponse
import com.example.modaurbanaprototipoapp.data.remote.dto.UserDto

class UserRepository(context: Context) {

    private val apiService: ApiService = RetrofitClient
        .create(context)
        .create(ApiService::class.java)

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): Result<UserDto> {
        return try {
            val user = apiService.getCurrentUser()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(id: Int): Result<UserDto> {
        return try {
            val user = apiService.getUserById(id)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}