package com.example.recipeapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.remote.model.DetailedMeal

import com.example.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: RecipeRepository) : ViewModel() {
    private val _mealDetail = MutableLiveData<DetailedMeal?>()
    val mealDetail: LiveData<DetailedMeal?> get() = _mealDetail

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchMealDetail(id: String) {
        viewModelScope.launch {
            val result = repository.getRecipeDetails(id)
            _mealDetail.postValue(result)
        }
    }
}