package com.example.recipeapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.api.RetrofitClient
import com.example.recipeapp.data.remote.model.DetailedMeal
import com.example.recipeapp.repository.RecipeRepository
import com.example.recipeapp.viewmodel.DetailViewModel
import com.example.recipeapp.viewmodel.ViewModelFactory

import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class RecipeDetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private val args: RecipeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = RecipeRepository(RetrofitClient.instance)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        // Initialize views
        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
        val collapsingToolbar: CollapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar)
        val recipeImageView: ImageView = view.findViewById(R.id.recipeImageView)
        val recipeTitleTextView: TextView = view.findViewById(R.id.recipeTitleTextView)
        val recipeCategoryTextView: TextView = view.findViewById(R.id.recipeCategoryTextView)
        val ingredientsTextView: TextView = view.findViewById(R.id.ingredientsTextView)
        val instructionsTextView: TextView = view.findViewById(R.id.instructionsTextView)
        val favoriteButton: ExtendedFloatingActionButton = view.findViewById(R.id.favoriteButton)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        // Set up toolbar navigation
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Observe recipe details
        viewModel.recipeDetail.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                displayRecipeDetails(it, collapsingToolbar, recipeImageView, recipeTitleTextView,
                    recipeCategoryTextView, ingredientsTextView, instructionsTextView)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        // Handle favorite button click
        favoriteButton.setOnClickListener {
            // TODO: Implement favorite functionality
            Toast.makeText(context, "Added to favorites!", Toast.LENGTH_SHORT).show()
        }

        // Fetch recipe details using the recipe ID passed from the previous screen
        viewModel.getRecipeDetails(args.recipeId)
    }

    private fun displayRecipeDetails(
        recipe: DetailedMeal,
        collapsingToolbar: CollapsingToolbarLayout,
        recipeImageView: ImageView,
        recipeTitleTextView: TextView,
        recipeCategoryTextView: TextView,
        ingredientsTextView: TextView,
        instructionsTextView: TextView
    ) {
        collapsingToolbar.title = recipe.strMeal
        recipeTitleTextView.text = recipe.strMeal

        // Display category and area information
        val categoryInfo = buildString {
            recipe.strCategory?.let { append("Category: $it") }
            if (recipe.strCategory != null && recipe.strArea != null) {
                append(" • ")
            }
            recipe.strArea?.let { append("Origin: $it") }
        }
        recipeCategoryTextView.text = categoryInfo.ifEmpty { "Category: Unknown" }

        // Load recipe image
        Glide.with(this)
            .load(recipe.strMealThumb)
            .centerCrop()
            .placeholder(R.drawable.placeholder_recipe) // You'll need to add this placeholder
            .error(R.drawable.placeholder_recipe)
            .into(recipeImageView)

        // Format and display ingredients
        val ingredients = formatIngredients(recipe)
        ingredientsTextView.text = ingredients

        // Display instructions
        val instructions = recipe.strInstructions?.trim()
        instructionsTextView.text = if (!instructions.isNullOrEmpty()) {
            formatInstructions(instructions)
        } else {
            "No instructions available."
        }
    }

    private fun formatIngredients(recipe: DetailedMeal): String {
        val ingredients = mutableListOf<String>()

        // The MealDB API returns ingredients and measurements in separate fields
        for (i in 1..20) {
            val ingredient = getIngredientByIndex(recipe, i)
            val measure = getMeasureByIndex(recipe, i)

            if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
                ingredients.add("• ${measure.trim()} ${ingredient.trim()}")
            } else if (!ingredient.isNullOrBlank()) {
                ingredients.add("• ${ingredient.trim()}")
            }
        }

        return if (ingredients.isNotEmpty()) {
            ingredients.joinToString("\n")
        } else {
            "No ingredients information available."
        }
    }

    private fun formatInstructions(instructions: String): String {
        // Split instructions by periods or numbered steps and format them nicely
        return instructions
            .replace("\\r\\n", "\n")
            .replace("\\n", "\n")
            .split(".")
            .filter { it.trim().isNotEmpty() }
            .mapIndexed { index, step ->
                "${index + 1}. ${step.trim()}."
            }
            .joinToString("\n\n")
    }

    private fun getIngredientByIndex(recipe: DetailedMeal, index: Int): String? {
        return when (index) {
            1 -> recipe.strIngredient1
            2 -> recipe.strIngredient2
            3 -> recipe.strIngredient3
            4 -> recipe.strIngredient4
            5 -> recipe.strIngredient5
            6 -> recipe.strIngredient6
            7 -> recipe.strIngredient7
            8 -> recipe.strIngredient8
            9 -> recipe.strIngredient9
            10 -> recipe.strIngredient10
            11 -> recipe.strIngredient11
            12 -> recipe.strIngredient12
            13 -> recipe.strIngredient13
            14 -> recipe.strIngredient14
            15 -> recipe.strIngredient15
            16 -> recipe.strIngredient16
            17 -> recipe.strIngredient17
            18 -> recipe.strIngredient18
            19 -> recipe.strIngredient19
            20 -> recipe.strIngredient20
            else -> null
        }
    }

    private fun getMeasureByIndex(recipe: DetailedMeal, index: Int): String? {
        return when (index) {
            1 -> recipe.strMeasure1
            2 -> recipe.strMeasure2
            3 -> recipe.strMeasure3
            4 -> recipe.strMeasure4
            5 -> recipe.strMeasure5
            6 -> recipe.strMeasure6
            7 -> recipe.strMeasure7
            8 -> recipe.strMeasure8
            9 -> recipe.strMeasure9
            10 -> recipe.strMeasure10
            11 -> recipe.strMeasure11
            12 -> recipe.strMeasure12
            13 -> recipe.strMeasure13
            14 -> recipe.strMeasure14
            15 -> recipe.strMeasure15
            16 -> recipe.strMeasure16
            17 -> recipe.strMeasure17
            18 -> recipe.strMeasure18
            19 -> recipe.strMeasure19
            20 -> recipe.strMeasure20
            else -> null
        }
    }
}
