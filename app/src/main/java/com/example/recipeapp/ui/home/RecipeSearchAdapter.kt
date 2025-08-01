package com.example.recipeapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.model.Meal

class RecipeSearchAdapter(
    private var recipes: List<Meal>,
    private val onItemClick: (Meal) -> Unit
) : RecyclerView.Adapter<RecipeSearchAdapter.RecipeSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeSearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_search, parent, false) // Using new layout
        return RecipeSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeSearchViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClick)
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<Meal>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    class RecipeSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImageView)
        private val recipeNameTextView: TextView = itemView.findViewById(R.id.recipeNameTextView)
        private val recipeDescriptionTextView: TextView = itemView.findViewById(R.id.recipeDescriptionTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        private val difficultyTextView: TextView = itemView.findViewById(R.id.difficultyTextView)


        fun bind(meal: Meal, onItemClick: (Meal) -> Unit) {
            recipeNameTextView.text = meal.strMeal
            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .centerCrop()
                .placeholder(R.drawable.placeholder_recipe)
                .error(R.drawable.placeholder_recipe)
                .into(recipeImageView)

            // Placeholder data for description, time, rating, difficulty
            // You might need to fetch these from a more detailed API or add them to your Meal model
            recipeDescriptionTextView.text = "A delicious and easy-to-make recipe for \"${meal.strMeal}\"."
            timeTextView.text = "${(30..60).random()} min" // Random time for now
            ratingTextView.text = String.format("%.1f", (3.5 + Math.random() * 1.5)) // Random rating

            val difficulty = listOf("Easy", "Medium", "Hard").random()
            difficultyTextView.text = difficulty
            when (difficulty) {
                "Easy" -> difficultyTextView.setBackgroundResource(R.drawable.difficulty_background_easy)
                "Medium" -> difficultyTextView.setBackgroundResource(R.drawable.difficulty_background_medium)
                "Hard" -> difficultyTextView.setBackgroundResource(R.drawable.difficulty_background_hard)
            }



            itemView.setOnClickListener {
                onItemClick(meal)
            }
        }
    }
}
