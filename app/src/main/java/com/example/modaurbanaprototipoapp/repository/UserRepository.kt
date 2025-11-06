package com.example.modaurbanaprototipoapp.repository

import android.content.Context
import com.example.modaurbanaprototipoapp.data.remote.ApiService
import com.example.modaurbanaprototipoapp.data.remote.RetrofitClient
import com.example.modaurbanaprototipoapp.data.remote.dto.*

class UserRepository(context: Context) {

    private val apiService: ApiService = RetrofitClient
        .create(context)
        .create(ApiService::class.java)

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email = email, password = password)
            val response = apiService.login(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signup(email: String, password: String, name: String): Result<SignupResponse> {
        return try {
            val request = SignupRequest(email = email, password = password, name = name)

            val respBasic = apiService.signupBasic(request)
            if (respBasic.isSuccessful) {
                return Result.success(respBasic.body()!!)
            }

            if (respBasic.code() == 404) {
                val respSimple = apiService.signupSimple(request)
                if (respSimple.isSuccessful) {
                    return Result.success(respSimple.body()!!)
                } else {
                    val msg = respSimple.errorBody()?.string()?.take(300) ?: "HTTP ${respSimple.code()}"
                    return Result.failure(Exception(msg))
                }
            } else {
                val msg = respBasic.errorBody()?.string()?.take(300) ?: "HTTP ${respBasic.code()}"
                Result.failure(Exception(msg))
            }
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
}
