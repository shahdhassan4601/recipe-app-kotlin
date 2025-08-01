package com.example.recipeapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<Meal>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImageView)
        private val recipeNameTextView: TextView = itemView.findViewById(R.id.recipeNameTextView)
        private val recipeDescriptionTextView: TextView = itemView.findViewById(R.id.recipeDescriptionTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val servingsTextView: TextView = itemView.findViewById(R.id.servingsTextView)




        fun bind(meal: Meal) {
            // Bind data
            recipeNameTextView.text = meal.strMeal
            recipeDescriptionTextView.text = "A delightful recipe of \"${meal.strMeal}\". Simple and delicious!"

            // Random dummy data
            timeTextView.text = "${(20..45).random()} min"
            servingsTextView.text = "${(2..5).random()} servings"

            // Load image
            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .centerCrop()
                .placeholder(R.drawable.placeholder_recipe)
                .error(R.drawable.placeholder_recipe)
                .into(recipeImageView)



            // Click to open details
            itemView.setOnClickListener {
                onItemClick(meal)
            }
        }

    }

}
