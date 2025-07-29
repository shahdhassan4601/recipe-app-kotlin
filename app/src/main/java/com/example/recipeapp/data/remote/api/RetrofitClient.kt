package com.example.recipeapp.data.remote.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.themealdb.com/"

    val instance: MealApiService by lazy {
        val retrofit = Retrofit.Builder( )
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(MealApiService::class.java)
    }
}