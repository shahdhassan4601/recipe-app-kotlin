package com.example.recipeapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapters.FavoriteAdapter
import com.example.recipeapp.databinding.FragmentFavoriteBinding
import com.example.recipeapp.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = SharedPrefsHelper.getUserId(requireContext())

        setupRecyclerView()
        observeFavoriteRecipes(userId)
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(
            onItemClick = { recipe ->
                val bundle = Bundle().apply {
                    putString("recipeId", recipe.idMeal)
                }
                findNavController().navigate(R.id.action_favorite_to_recipeDetail, bundle)
            },
            onRemoveClick = { recipe ->
                favoriteViewModel.removeFavorite(recipe)
            }
        )

        binding.recyclerViewFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }
    }

    private fun observeFavoriteRecipes(userId: Int) {
        favoriteViewModel.getFavorites(userId).observe(viewLifecycleOwner) { recipes ->
            if (recipes.isEmpty()) {
                binding.textViewEmpty.visibility = View.VISIBLE
                binding.recyclerViewFavorites.visibility = View.GONE
            } else {
                binding.textViewEmpty.visibility = View.GONE
                binding.recyclerViewFavorites.visibility = View.VISIBLE
                favoriteAdapter.submitList(recipes)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
