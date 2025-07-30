package com.example.recipeapp.di

import com.example.recipeapp.data.remote.api.RetrofitClient
import com.example.recipeapp.repository.RecipeRepository

object RepositoryProvider {
    fun provideRecipeRepository(): RecipeRepository {
        return RecipeRepository(RetrofitClient.instance)
    }
}
