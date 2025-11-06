package com.example.modaurbanaprototipoapp.data.remote

import com.example.modaurbanaprototipoapp.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getCurrentUser(): UserDto

    @POST("auth/signup/basic")
    suspend fun signupBasic(@Body request: SignupRequest): Response<SignupResponse>

    @POST("auth/signup")
    suspend fun signupSimple(@Body request: SignupRequest): Response<SignupResponse>
}
