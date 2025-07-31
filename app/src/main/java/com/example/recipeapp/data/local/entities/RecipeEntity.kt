package com.example.recipeapp.data.local.entities



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class RecipeEntity(
    @PrimaryKey
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strYoutube: String?,
    // Store ingredients as JSON - completely dynamic, any number
    val ingredientsJson: String, // JSON array of Ingredient objects
    val dateAdded: Long = System.currentTimeMillis()

)

// Helper data class for ingredients with measure (quantity)
data class Ingredient(
    val name: String,        // e.g., "Chicken Breast", "Salt", "Onion"
    val measure: String      // e.g., "2 lbs", "1 tsp", "1 large"
) {
    // Helper to display ingredient with measure
    fun getDisplayText(): String {
        return if (measure.isNotBlank()) {
            "$measure $name"  // "2 lbs Chicken Breast"
        } else {
            name  // Just "Salt" if no measure
        }
    }
}