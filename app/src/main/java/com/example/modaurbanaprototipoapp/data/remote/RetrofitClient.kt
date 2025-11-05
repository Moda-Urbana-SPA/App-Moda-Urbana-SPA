package com.example.modaurbanaprototipoapp.data.remote

import android.content.Context
import com.example.modaurbanaprototipoapp.data.local.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // ⚠️ CAMBIA ESTA URL POR LA DE TU API
    private const val BASE_URL = "https://dummyjson.com/"

    fun create(context: Context): Retrofit {

        val sessionManager = SessionManager(context)
        val authInterceptor = AuthInterceptor(sessionManager)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}