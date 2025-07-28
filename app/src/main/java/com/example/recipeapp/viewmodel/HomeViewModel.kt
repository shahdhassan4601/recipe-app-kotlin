package com.example.recipeapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.remote.model.Meal

import com.example.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _recipes = MutableLiveData<List<Meal>>()
    val recipes: LiveData<List<Meal>> = _recipes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getRecipesByCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getRecipesByCategory(category)
            _recipes.value = result ?: emptyList()
            _isLoading.value = false
        }
    }
}