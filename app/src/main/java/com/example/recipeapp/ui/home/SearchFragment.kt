package com.example.recipeapp.ui.home

import com.example.recipeapp.repository.RecipeRepository
import com.example.recipeapp.viewmodel.SearchViewModel
import com.example.recipeapp.viewmodel.ViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.api.RetrofitClient

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchAdapter: RecipeSearchAdapter
    private lateinit var categoryButtons: List<Button>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = RecipeRepository(RetrofitClient.instance)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        val searchEditText: EditText = view.findViewById(R.id.searchEditText)
        val searchResultsRecyclerView: RecyclerView = view.findViewById(R.id.searchResultsRecyclerView)
        val searchProgressBar: ProgressBar = view.findViewById(R.id.searchProgressBar)

        searchAdapter = RecipeSearchAdapter(emptyList()) { meal ->
            val action = SearchFragmentDirections.actionSearchFragmentToRecipeDetailFragment(meal.idMeal ?: "")
            findNavController().navigate(action)
        }

        searchResultsRecyclerView.layoutManager = LinearLayoutManager(context)
        searchResultsRecyclerView.adapter = searchAdapter

        viewModel.searchResults.observe(viewLifecycleOwner) { recipes ->
            searchAdapter.updateRecipes(recipes)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            searchProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        searchEditText.addTextChangedListener { text ->
            viewModel.searchRecipes(text.toString())
        }

        val btnAll = view.findViewById<Button>(R.id.btnAll)
        val btnBeef = view.findViewById<Button>(R.id.btnBeef)
        val btnChicken = view.findViewById<Button>(R.id.btnChicken)
        val btnSeafood = view.findViewById<Button>(R.id.btnSeafood)

        val btnVegan = view.findViewById<Button>(R.id.btnVegan)

        categoryButtons = listOf(btnAll, btnBeef, btnChicken, btnSeafood, btnVegan)

        fun highlightSelectedButton(selectedButton: Button) {
            categoryButtons.forEach { button ->
                if (button == selectedButton) {
                    button.setBackgroundResource(R.drawable.category_button_selected)
                    button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                } else {
                    button.setBackgroundResource(R.drawable.category_button_unselected)
                    button.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryColor))
                }
            }
        }

        fun setupCategoryButton(button: Button, category: String?) {
            button.setOnClickListener {
                animateButton(button)
                highlightSelectedButton(button)
                if (category.isNullOrEmpty()) {
                    viewModel.searchRecipes("")
                } else {
                    viewModel.searchRecipes(category)
                }
            }
        }

        setupCategoryButton(btnAll, "")
        setupCategoryButton(btnBeef, "Beef")
        setupCategoryButton(btnChicken, "Chicken")
        setupCategoryButton(btnSeafood, "Seafood")
        setupCategoryButton(btnVegan, "Vegan")
    }

    private fun animateButton(button: View) {
        button.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                button.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .duration = 100
            }
    }
}
