package com.example.modaurbanaprototipoapp.data.remote

import com.example.modaurbanaprototipoapp.data.remote.dto.*
import retrofit2.http.*

interface ApiService {

    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("user/me")
    suspend fun getCurrentUser(): UserDto

    @GET("users")
    suspend fun getUsers(): UsersResponse

    @GET("users/search")
    suspend fun searchUsers(@Query("q") query: String): UsersResponse

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDto
}