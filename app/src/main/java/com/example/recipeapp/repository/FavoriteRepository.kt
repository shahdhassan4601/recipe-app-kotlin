package com.example.recipeapp.repository

import androidx.lifecycle.LiveData
import com.example.recipeapp.data.local.AppDatabase
import com.example.recipeapp.data.local.entities.FavoriteRecipeEntity

class FavoriteRepository(private val db: AppDatabase) {

    fun getFavorites(userId: Int): LiveData<List<FavoriteRecipeEntity>> {
        return db.favoriteDao().getFavoritesByUserId(userId)
    }

    suspend fun addFavorite(recipe: FavoriteRecipeEntity) {
        db.favoriteDao().addToFavorites(recipe)
    }

    suspend fun removeFavorite(recipe: FavoriteRecipeEntity) {
        db.favoriteDao().removeFromFavorites(recipe)
    }

    suspend fun removeFavoriteById(idMeal: String, userId: Int) {
        db.favoriteDao().deleteById(idMeal, userId)
    }

    suspend fun isFavorited(idMeal: String, userId: Int): Boolean {
        return db.favoriteDao().isRecipeFavorited(idMeal, userId)
    }
}
