package com.example.recipeapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.remote.model.Meal

import com.example.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Meal>>()
    val searchResults: LiveData<List<Meal>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.searchRecipes(query)
            _searchResults.value = result ?: emptyList()
            _isLoading.value = false
        }
    }
}