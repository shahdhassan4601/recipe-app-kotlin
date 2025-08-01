package com.example.recipeapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.api.RetrofitClient
import com.example.recipeapp.repository.RecipeRepository
import com.example.recipeapp.ui.auth.AuthActivity

import com.example.recipeapp.viewmodel.HomeViewModel
import com.example.recipeapp.viewmodel.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
class HomeFragment : Fragment( ) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
                    true
                }
                R.id.action_logout -> {
                    SharedPrefsHelper.logout(requireContext())

                    // Kill RecipeActivity and return to Splash screen
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), AuthActivity::class.java)) // assuming AuthActivity hosts SplashFragment
                    true
                }
                else -> false
            }
        }

        val repository = RecipeRepository(RetrofitClient.instance)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        // Set up RecyclerView with GridLayoutManager for a more modern look
        recipeAdapter = RecipeAdapter(emptyList()) { meal ->
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(meal.idMeal)
            findNavController().navigate(action)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recipeAdapter

        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            recipeAdapter.updateRecipes(recipes)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Fetch initial recipes - you can change the category as needed
        viewModel.getRecipesByCategory("Seafood")
    }
}