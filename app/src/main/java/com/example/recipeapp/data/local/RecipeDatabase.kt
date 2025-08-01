package com.example.recipeapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipeapp.data.local.dao.RecipeDAO
import com.example.recipeapp.data.local.dao.UserDAO
import com.example.recipeapp.data.local.entities.RecipeEntity
import com.example.recipeapp.data.local.entities.UserEntity

@Database(
    entities = [RecipeEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDAO
    abstract fun userDao(): UserDAO


    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}