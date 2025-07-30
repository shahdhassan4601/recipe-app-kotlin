package com.example.recipeapp.data.remote.api

import com.example.recipeapp.data.remote.model.ApiResponse
import com.example.recipeapp.data.remote.model.DetailApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {

    @GET("api/json/v1/1/search.php" )
    suspend fun searchRecipes(@Query("s") query: String): Response<ApiResponse>

    @GET("api/json/v1/1/filter.php")
    suspend fun getRecipesByCategory(@Query("c") category: String): Response<ApiResponse>

    @GET("api/json/v1/1/lookup.php")
    suspend fun getRecipeDetails(@Query("i") recipeId: String): Response<DetailApiResponse>
}