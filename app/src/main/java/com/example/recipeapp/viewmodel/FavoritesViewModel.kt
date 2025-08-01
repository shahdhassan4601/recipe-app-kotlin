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

    private val _isFavorited = MutableLiveData<Boolean>()
    val isFavorited: LiveData<Boolean> get() = _isFavorited

    fun getFavorites(userId: Int): LiveData<List<FavoriteRecipeEntity>> {
        return repository.getFavorites(userId)
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

    fun removeFavoriteById(idMeal: String, userId: Int) {
        viewModelScope.launch {
            repository.removeFavoriteById(idMeal, userId)
        }
    }

    suspend fun isFavorited(idMeal: String, userId: Int): Boolean {
        return repository.isFavorited(idMeal, userId)
    }

    fun checkIfFavorited(idMeal: String, userId: Int) {
        viewModelScope.launch {
            val result = repository.isFavorited(idMeal, userId)
            _isFavorited.postValue(result)
        }
    }
}
