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
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.clothingDao())
                }
            }
        }

        /**
         * Productos reales de Moda Urbana
         * Basados en el catálogo real de la tienda
         */
        suspend fun populateDatabase(clothingDao: ClothingDao) {
            val initialClothing = listOf(
                // 1. Poleron Negro Fatto
                ClothingItem(
                    name = "Poleron Negro Fatto",
                    description = "Poleron urbano negro de alta calidad, ideal para el día a día",
                    price = 69990,
                    category = "Polerones",
                    imageUrl = "https://media.istockphoto.com/id/1675347103/es/foto/sudadera-en-blanco-color-negro-plantilla-vista-delantera-y-trasera-sobre-fondo-blanco-maqueta.jpg?b=1&s=612x612&w=0&k=20&c=j8d5Ry9jsE5CNVHhlbH5XCTUaYRhm7r27ZraRoVeYiw=",
                    stock = 12, // S(5) + M(3) + M(4) = 12 unidades totales
                    brand = "Fatto"
                ),

                // 2. Camiseta Blanca S/M
                ClothingItem(
                    name = "Camiseta Blanca S/M",
                    description = "Camiseta básica blanca, corte clásico, perfecta para combinar",
                    price = 8990,
                    category = "Camisetas",
                    imageUrl = "https://media.istockphoto.com/id/1151955708/es/foto/plantilla-de-camiseta-blanca-en-blanco-para-hombre-de-dos-lados-forma-natural-en-maniqu%C3%AD.jpg?b=1&s=612x612&w=0&k=20&c=Ug3VKi5U0A_sVvrfjdjoKyi2Ajq8NpKCUBjAutkXHM0=",
                    stock = 15, // S(6) + M(4) + L(2) + M(2) + L(1) = 15
                    brand = "Moda Urbana"
                ),

                // 3. Pantalón Baggy Gris
                ClothingItem(
                    name = "Pantalón Baggy Gris",
                    description = "Pantalón baggy de corte holgado, tendencia urbana actual",
                    price = 39990,
                    category = "Pantalones",
                    imageUrl = "https://media.falabella.com/falabellaCL/883453604_1/public",
                    stock = 14, // S(5) + M(4) + L(3) + M(2) = 14
                    brand = "Moda Urbana"
                ),

                // 4. Bolso de Mano Fatto
                ClothingItem(
                    name = "Bolso de Mano Fatto",
                    description = "Bolso urbano de alta calidad, diseño minimalista y funcional",
                    price = 89990,
                    category = "Accesorios",
                    imageUrl = "https://http2.mlstatic.com/D_NQ_NP_949458-MLU78166593109_082024-O.webp",
                    stock = 11, // Negro(8) + Gris(3) = 11
                    brand = "Fatto"
                ),

                // 5. Zapatillas Skate
                ClothingItem(
                    name = "Zapatillas Skate",
                    description = "Zapatillas estilo skate, suela antideslizante, máxima durabilidad",
                    price = 79990,
                    category = "Accesorios",
                    imageUrl = "https://images.pexels.com/photos/6389841/pexels-photo-6389841.jpeg",
                    stock = 7, // 40(2) + 41(2) + 42(2) + 41(1) = 7
                    brand = "Moda Urbana"
                ),

                // 6. Polera Regular Fit
                ClothingItem(
                    name = "Polera Regular Fit",
                    description = "Polera de corte regular, algodón premium, disponible en varios colores",
                    price = 19990,
                    category = "Poleras",
                    imageUrl = "https://cl-dam-resizer.ecomm.cencosud.com/unsafe/adaptive-fit-in/3840x0/filters:quality(75)/cl/paris/780528/variant/67e2989909ea098d9ff2319d/images/221e4c70-32cd-4b71-af1d-bf5916c3ded5/780528-0001-001.jpg",
                    stock = 15, // S(3) + M(3) + L(3) + S(2) + M(2) + L(2) = 15
                    brand = "Moda Urbana"
                ),

                // 7. Poleron Gris Thrasher
                ClothingItem(
                    name = "Poleron Gris Thrasher",
                    description = "Poleron gris estilo skate, logo Thrasher, 100% algodón",
                    price = 69990,
                    category = "Polerones",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1eyEoIisWcbxTrcuNlBmG7ahhx3YaWLf1Vw&s",
                    stock = 12, // S(5) + M(4) + M(2) + L(1) = 12
                    brand = "Thrasher"
                ),

                // 8. Pantalón Loose Fit
                ClothingItem(
                    name = "Pantalón Loose Fit",
                    description = "Pantalón de corte loose fit, comodidad y estilo urbano",
                    price = 32990,
                    category = "Pantalones",
                    imageUrl = "https://m.media-amazon.com/images/I/71xppkZgaHL._AC_UY1000_.jpg",
                    stock = 13, // S(3) + M(4) + L(2) + M(2) + L(2) = 13
                    brand = "Moda Urbana"
                )
            )

            clothingDao.insertAllClothing(initialClothing)
        }
    }
}