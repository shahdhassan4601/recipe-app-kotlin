package com.example.recipeapp.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R

class IngredientsAdapter(private val ingredients: List<Ingredient>):
    RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMeasure: TextView = itemView.findViewById(R.id.measureText)
        val tvIngredient: TextView = itemView.findViewById(R.id.ingredientText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_item, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val item = ingredients[position]
        holder.tvMeasure.text = item.measure
        holder.tvIngredient.text = item.name
    }

    override fun getItemCount(): Int = ingredients.size

}