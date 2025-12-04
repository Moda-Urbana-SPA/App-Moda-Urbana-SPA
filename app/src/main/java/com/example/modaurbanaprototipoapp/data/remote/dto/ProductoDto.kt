package com.example.modaurbanaprototipoapp.data.remote.dto

data class CategoriaDto(
    val _id: String? = null,
    val nombre: String? = null,
    val descripcion: String? = null
)

data class ProductoDto(
    val _id: String? = null,
    val nombre: String? = null,
    val descripcion: String? = null,
    val talla: String? = null,
    val material: String? = null,
    val genero: String? = null,
    val estilo: String? = null,
    val categoria: CategoriaDto? = null,
    val imagen: String? = null,
    val imagenThumbnail: String? = null,
    val precio: Int? = null,
    val stock: Int? = null
)
