package com.example.modaurbanaprototipoapp.data.remote

import com.example.modaurbanaprototipoapp.data.remote.dto.LoginRequest
import com.example.modaurbanaprototipoapp.data.remote.dto.LoginResponse
import com.example.modaurbanaprototipoapp.data.remote.dto.SignupRequest
import com.example.modaurbanaprototipoapp.data.remote.dto.SignupResponse
import com.example.modaurbanaprototipoapp.data.remote.dto.ProfileResponse
import com.example.modaurbanaprototipoapp.data.remote.dto.ProductosResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("auth/profile")
    suspend fun getCurrentUser(): ProfileResponse

    @POST("auth/register")
    suspend fun register(@Body request: SignupRequest): SignupResponse

    @GET("producto")
    suspend fun getProductos(): ProductosResponse
}
