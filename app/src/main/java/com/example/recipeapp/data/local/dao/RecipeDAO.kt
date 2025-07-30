package com.example.recipeapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipeapp.data.local.entities.RecipeEntity


@Dao
interface RecipeDAO {

    @Query("SELECT * FROM favorite_recipes ORDER BY dateAdded DESC")
    fun getAllFavoriteRecipes(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM favorite_recipes WHERE idMeal = :recipeId")
    suspend fun getFavoriteRecipeById(recipeId: String): RecipeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteFavoriteRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM favorite_recipes WHERE idMeal = :recipeId")
    suspend fun deleteFavoriteRecipeById(recipeId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE idMeal = :recipeId)")
    suspend fun isRecipeFavorite(recipeId: String): Boolean

    @Query("SELECT COUNT(*) FROM favorite_recipes")
    suspend fun getFavoriteCount(): Int
}