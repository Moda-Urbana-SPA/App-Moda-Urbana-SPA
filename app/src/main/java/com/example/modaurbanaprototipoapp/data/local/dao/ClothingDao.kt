package com.example.modaurbanaprototipoapp.data.local.dao

import androidx.room.*
import com.example.modaurbanaprototipoapp.data.local.entity.ClothingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClothing(item: ClothingItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllClothing(items: List<ClothingItem>)

    @Query("SELECT * FROM clothing_items WHERE isAvailable = 1 ORDER BY id DESC")
    fun getAllClothing(): Flow<List<ClothingItem>>

    @Query("SELECT * FROM clothing_items WHERE category = :category AND isAvailable = 1")
    fun getClothingByCategory(category: String): Flow<List<ClothingItem>>

    @Query("SELECT * FROM clothing_items WHERE name LIKE '%' || :query || '%' AND isAvailable = 1")
    fun searchClothing(query: String): Flow<List<ClothingItem>>

    @Query("SELECT * FROM clothing_items WHERE id = :id")
    suspend fun getClothingById(id: Int): ClothingItem?

    @Update
    suspend fun updateClothing(item: ClothingItem)

    @Query("DELETE FROM clothing_items")
    suspend fun deleteAllClothing()
}