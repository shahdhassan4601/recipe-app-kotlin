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
class RecipeAdapter(
    private var recipes: List<Meal>,
    private val onItemClick: (Meal) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClick)
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<Meal>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImageView)
        private val recipeNameTextView: TextView = itemView.findViewById(R.id.recipeNameTextView)
        private val recipeDescriptionTextView: TextView = itemView.findViewById(R.id.recipeDescriptionTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val servingsTextView: TextView = itemView.findViewById(R.id.servingsTextView)

        fun bind(meal: Meal, onItemClick: (Meal) -> Unit) {
            recipeNameTextView.text = meal.strMeal
            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .centerCrop()
                .placeholder(R.drawable.placeholder_recipe)
                .error(R.drawable.placeholder_recipe)
                .into(recipeImageView)

            // Placeholder data for description, time, and servings
            // Similar to search, these might need to come from a more detailed API or be added to your Meal model
            recipeDescriptionTextView.text = "A delightful recipe for \"${meal.strMeal}\" that is easy to prepare."
            timeTextView.text = "${(20..50).random()} min" // Random time for now
            servingsTextView.text = "${(2..6).random()} servings" // Random servings for now

            itemView.setOnClickListener {
                onItemClick(meal)
            }
        }
    }
}