package com.example.recipeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.AppDatabase
import com.example.recipeapp.data.local.entities.FavoriteRecipeEntity
import com.example.recipeapp.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = FavoriteRepository(db)

    private val _userId = MutableLiveData<Int>()

    val favoriteRecipes: LiveData<List<FavoriteRecipeEntity>> =
        _userId.switchMap { userId ->
            repository.getFavorites(userId)
        }

    fun setUserId(userId: Int) {
        _userId.value = userId
    }

    fun addFavorite(recipe: FavoriteRecipeEntity) {
        viewModelScope.launch {
            repository.addFavorite(recipe)
        }
    }

    fun removeFavorite(recipe: FavoriteRecipeEntity) {
        viewModelScope.launch {
            repository.removeFavorite(recipe)
        }
    }

    suspend fun isFavorited(idMeal: String, userId: Int): Boolean {
        return repository.isFavorited(idMeal, userId)
    }
}
