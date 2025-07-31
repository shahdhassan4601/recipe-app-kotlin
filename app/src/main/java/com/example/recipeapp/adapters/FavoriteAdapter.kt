package com.example.recipeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.local.entities.FavoriteRecipeEntity
import com.example.recipeapp.databinding.ItemFavoriteRecipeBinding

class FavoriteAdapter(
    private val onItemClick: (FavoriteRecipeEntity) -> Unit,
    private val onRemoveClick: (FavoriteRecipeEntity) -> Unit
) : ListAdapter<FavoriteRecipeEntity, FavoriteAdapter.FavoriteViewHolder>(DiffCallback()) {

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

        fun bind(recipe: FavoriteRecipeEntity) {
            binding.apply {
                textViewRecipeName.text = recipe.mealName
                textViewCategory.text = recipe.category ?: "Unknown Category"

                Glide.with(imageViewRecipe.context)
                    .load(recipe.mealThumb)
                    .placeholder(R.drawable.placeholder_recipe)
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

    class DiffCallback : DiffUtil.ItemCallback<FavoriteRecipeEntity>() {
        override fun areItemsTheSame(
            oldItem: FavoriteRecipeEntity,
            newItem: FavoriteRecipeEntity
        ): Boolean = oldItem.idMeal == newItem.idMeal && oldItem.userId == newItem.userId

        override fun areContentsTheSame(
            oldItem: FavoriteRecipeEntity,
            newItem: FavoriteRecipeEntity
        ): Boolean = oldItem == newItem
    }
}
