package com.example.recipeapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.remote.model.DetailedMeal

import com.example.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: RecipeRepository) : ViewModel() {

}