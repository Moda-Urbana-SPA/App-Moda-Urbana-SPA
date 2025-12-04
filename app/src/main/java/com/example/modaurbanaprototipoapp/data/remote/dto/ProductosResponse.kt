package com.example.modaurbanaprototipoapp.data.remote.dto

data class ProductosResponse(
    val success: Boolean,
    val data: List<ProductoDto>,
    val total: Int
)
