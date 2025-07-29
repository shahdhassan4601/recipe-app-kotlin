package com.example.recipeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.RecipeDatabase
import com.example.recipeapp.data.local.entities.RecipeEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()

    val favoriteRecipes: LiveData<List<RecipeEntity>> = recipeDao.getAllFavoriteRecipes()

    fun addFavorite(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeDao.insertFavoriteRecipe(recipe)
        }
    }

    fun removeFavorite(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeDao.deleteFavoriteRecipe(recipe)
        }
    }

    fun removeFavoriteById(recipeId: String) {
        viewModelScope.launch {
            recipeDao.deleteFavoriteRecipeById(recipeId)
        }
    }

    suspend fun isRecipeFavorite(recipeId: String): Boolean {
        return recipeDao.isRecipeFavorite(recipeId)
    }

    suspend fun getFavoriteCount(): Int {
        return recipeDao.getFavoriteCount()
    }
}