package com.example.recipeapp.adapters



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.local.entities.RecipeEntity
import com.example.recipeapp.databinding.ItemFavoriteRecipeBinding


class FavoriteAdapter(
    private val onItemClick: (RecipeEntity) -> Unit,
    private val onRemoveClick: (RecipeEntity) -> Unit
) : ListAdapter<RecipeEntity, FavoriteAdapter.FavoriteViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteRecipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteViewHolder(
        private val binding: ItemFavoriteRecipeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeEntity) {
            binding.apply {
                textViewRecipeName.text = recipe.strMeal
                textViewCategory.text = recipe.strCategory ?: "Unknown Category"
                textViewArea.text = recipe.strArea ?: "Unknown Area"

//                // Show ingredients count
//                val ingredientsCount = recipe.getIngredientsCount()
//                textViewIngredientsCount.text = "$ingredientsCount ingredients"

                Glide.with(imageViewRecipe.context)
                    .load(recipe.strMealThumb)
                    .placeholder(R.drawable.placeholder_recipe)
//                    .error(R.drawable.error_recipe)
                    .into(imageViewRecipe)

                root.setOnClickListener {
                    onItemClick(recipe)
                }

                buttonRemove.setOnClickListener {
                    onRemoveClick(recipe)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RecipeEntity>() {
        override fun areItemsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
            return oldItem == newItem
        }
    }
}