package com.example.recipeapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteRecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idMeal: String, // Assuming API provides this as unique ID
    val userId: Int,    // Foreign key reference to UserEntity
    val mealName: String,
    val mealThumb: String,
    val category: String?
)
