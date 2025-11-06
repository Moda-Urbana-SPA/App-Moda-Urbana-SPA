package com.example.modaurbanaprototipoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val username: String,
    val password: String,
    val firstName: String = "",
    val lastName: String = "",
    val image: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)