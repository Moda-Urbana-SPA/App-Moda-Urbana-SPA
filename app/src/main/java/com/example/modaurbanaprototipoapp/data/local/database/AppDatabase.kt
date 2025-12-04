package com.example.modaurbanaprototipoapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.modaurbanaprototipoapp.data.local.dao.ClothingDao
import com.example.modaurbanaprototipoapp.data.local.dao.UserDao
import com.example.modaurbanaprototipoapp.data.local.entity.ClothingCategory
import com.example.modaurbanaprototipoapp.data.local.entity.ClothingItem
import com.example.modaurbanaprototipoapp.data.local.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, ClothingItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun clothingDao(): ClothingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "moda_urbana_database"
                )
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }
    }

}