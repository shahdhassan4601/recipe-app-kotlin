package com.example.recipeapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipeapp.data.local.entities.FavoriteRecipeEntity

@Dao
interface FavoriteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(recipe: FavoriteRecipeEntity)

    @Delete
    suspend fun removeFromFavorites(recipe: FavoriteRecipeEntity)

    @Query("SELECT * FROM favorites WHERE userId = :userId")
    fun getFavoritesByUserId(userId: Int): LiveData<List<FavoriteRecipeEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE idMeal = :idMeal AND userId = :userId)")
    suspend fun isRecipeFavorited(idMeal: String, userId: Int): Boolean
}