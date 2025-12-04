package com.example.modaurbanaprototipoapp.data.local.entity

import com.example.modaurbanaprototipoapp.data.remote.dto.ProductoDto

private const val IMAGE_BASE_URL = "https://modaurbana-api-hoo9.onrender.com"

private fun buildImageUrl(path: String?): String {
    if (path.isNullOrBlank()) return ""
    return if (path.startsWith("http")) {
        path
    } else {
        "$IMAGE_BASE_URL/$path".replace("//", "/")
    }
}

fun ProductoDto.toClothingItem(): com.example.modaurbanaprototipoapp.data.local.entity.ClothingItem {
    val categoriaNombre = this.categoria?.nombre ?: "Sin categoría"

    return com.example.modaurbanaprototipoapp.data.local.entity.ClothingItem(
        name = this.nombre ?: "Producto sin nombre",
        description = this.descripcion ?: "",
        price = this.precio ?: 0,
        category = categoriaNombre,
        imageUrl = buildImageUrl(this.imagen),
        stock = this.stock ?: 10,
        brand = "Moda Urbana",
        isAvailable = true
    )
}
