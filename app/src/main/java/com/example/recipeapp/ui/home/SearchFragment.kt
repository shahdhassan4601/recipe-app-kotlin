package com.example.recipeapp.ui.home

import com.example.recipeapp.repository.RecipeRepository
import com.example.recipeapp.viewmodel.SearchViewModel
import com.example.recipeapp.viewmodel.ViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.api.RetrofitClient
import com.example.recipeapp.ui.home.RecipeAdapter

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchAdapter: RecipeSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            val action = SearchFragmentDirections.actionSearchFragmentToRecipeDetailFragment(meal.idMeal)
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
            if (text.toString().isNotEmpty()) {
                viewModel.searchRecipes(text.toString())
            }
        }

        // Implement category button click listeners to filter search results
        view.findViewById<Button>(R.id.btnBreakfast).setOnClickListener {
            viewModel.searchRecipes("Breakfast")
        }
        view.findViewById<Button>(R.id.btnLunch).setOnClickListener {
            viewModel.searchRecipes("Lunch")
        }
        view.findViewById<Button>(R.id.btnDinner).setOnClickListener {
            viewModel.searchRecipes("Dinner")
        }

    }
}