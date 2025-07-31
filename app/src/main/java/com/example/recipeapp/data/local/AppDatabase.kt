package com.example.recipeapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp.data.local.dao.UserDAO
import com.example.recipeapp.data.local.entities.UserEntity
import androidx.room.Room
import com.example.recipeapp.data.local.dao.FavoriteDAO
import com.example.recipeapp.data.local.entities.FavoriteRecipeEntity

@Database(
    entities = [UserEntity::class, FavoriteRecipeEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun favoriteDao(): FavoriteDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipe_database"
                )
                    .fallbackToDestructiveMigration() //  This will delete all existing user data when schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
