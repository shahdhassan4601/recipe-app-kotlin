package com.example.recipeapp.repository


import com.example.recipeapp.data.remote.api.MealApiService
import com.example.recipeapp.data.remote.model.DetailedMeal
import com.example.recipeapp.data.remote.model.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(private val mealApiService: MealApiService) {

    suspend fun searchRecipes(query: String): List<Meal>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = mealApiService.searchRecipes(query)
                if (response.isSuccessful) {
                    response.body()?.meals
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByCategory(category: String): List<Meal>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = mealApiService.getRecipesByCategory(category)
                if (response.isSuccessful) {
                    response.body()?.meals
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipeDetails(recipeId: String): DetailedMeal? {
        return withContext(Dispatchers.IO) {
            try {
                val response = mealApiService.getRecipeDetails(recipeId)
                if (response.isSuccessful) {
                    response.body()?.meals?.firstOrNull()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

}