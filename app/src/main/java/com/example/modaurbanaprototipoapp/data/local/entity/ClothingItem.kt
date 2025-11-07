package com.example.modaurbanaprototipoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clothing_items")
data class ClothingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Int, // Precio en CLP
    val category: String,
    val imageUrl: String,
    val stock: Int = 10,
    val brand: String = "Moda Urbana",
    val isAvailable: Boolean = true
)

object ClothingCategory {
    const val POLERONES = "Polerones"
    const val PANTALONES = "Pantalones"
    const val CAMISETAS = "Camisetas"
    const val ACCESORIOS = "Accesorios"
    const val POLERAS = "Poleras"
}