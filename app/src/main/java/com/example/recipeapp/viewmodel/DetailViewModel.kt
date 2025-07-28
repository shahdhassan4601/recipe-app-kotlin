package com.example.recipeapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.remote.model.DetailedMeal

import com.example.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _recipeDetail = MutableLiveData<DetailedMeal?>()
    val recipeDetail: LiveData<DetailedMeal?> = _recipeDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getRecipeDetails(recipeId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = repository.getRecipeDetails(recipeId)
                _recipeDetail.value = result

                if (result == null) {
                    _error.value = "Recipe not found"
                }
            } catch (e: Exception) {
                _error.value = "Failed to load recipe details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}