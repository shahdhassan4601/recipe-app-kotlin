package com.example.recipeapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.local.entities.FavoriteRecipeEntity
import com.example.recipeapp.data.remote.model.DetailedMeal
import com.example.recipeapp.databinding.FragmentRecipeDetailBinding
import com.example.recipeapp.di.RepositoryProvider
import com.example.recipeapp.viewmodel.DetailViewModel
import com.example.recipeapp.viewmodel.DetailViewModelFactory
import com.example.recipeapp.viewmodel.FavoriteViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

/**
 * Fragment for displaying detailed information about a selected recipe.
 * Handles recipe details display, favorite functionality, and video playback.
 */
class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var instructionsAdapter: InstructionsAdapter
    private lateinit var youtubePlayerView: YouTubePlayerView

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(RepositoryProvider.provideRecipeRepository())
    }
    private val favoritesViewModel: FavoriteViewModel by viewModels()
    private val args: RecipeDetailFragmentArgs by navArgs()

    private var isFavorite = false
    private var currentUserId: Int = -1
    private var currentMeal: DetailedMeal? = null
    private var isIngredientsExpanded = false
    private var isInstructionsExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        setupViewModelObservers()
        setupUserAndFavoriteHandling()
    }

    /**
     * Initializes UI components and sets up click listeners.
     */
    private fun initializeViews() {
        youtubePlayerView = binding.videoOverlayInclude.youtubePlayerView
        lifecycle.addObserver(youtubePlayerView)

        setupViews()
        setupCollapsibleSections()
        setupRecyclerViews()
    }

    /**
     * Sets up button click listeners for navigation and video playback.
     */
    private fun setupViews() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.watchVideoButton.setOnClickListener {
            currentMeal?.strYoutube?.let { url ->
                val videoId = extractYoutubeVideoId(url)
                if (videoId.isNotBlank()) {
                    playVideo(videoId)
                }
            }
        }
    }

    /**
     * Configures collapsible sections for ingredients and instructions.
     */
    private fun setupCollapsibleSections() {
        binding.ingredientsSection.ingredientsRecyclerView.visibility = View.GONE
        binding.instructionsSection.instructionsRecyclerView.visibility = View.GONE
        binding.ingredientsSection.ingredientsToggleIcon.setImageResource(R.drawable.chevron_down)
        binding.instructionsSection.instructionsToggleIcon.setImageResource(R.drawable.chevron_down)

        binding.ingredientsSection.ingredientsHeader.setOnClickListener {
            isIngredientsExpanded = !isIngredientsExpanded
            toggleVisibilityWithAnimation(
                binding.ingredientsSection.ingredientsRecyclerView,
                isIngredientsExpanded
            )
            binding.ingredientsSection.ingredientsToggleIcon.setImageResource(
                if (isIngredientsExpanded) R.drawable.chevron_up else R.drawable.chevron_down
            )
        }

        binding.instructionsSection.instructionsHeader.setOnClickListener {
            isInstructionsExpanded = !isInstructionsExpanded
            toggleVisibilityWithAnimation(
                binding.instructionsSection.instructionsRecyclerView,
                isInstructionsExpanded
            )
            binding.instructionsSection.instructionsToggleIcon.setImageResource(
                if (isInstructionsExpanded) R.drawable.chevron_up else R.drawable.chevron_down
            )
        }
    }

    /**
     * Animates visibility changes for collapsible sections.
     */
    private fun toggleVisibilityWithAnimation(view: View, expand: Boolean) {
        val animation = if (expand) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
        animation.duration = 300
        view.startAnimation(animation)
        view.visibility = if (expand) View.VISIBLE else View.GONE
    }

    /**
     * Configures RecyclerViews for ingredients and instructions.
     */
    private fun setupRecyclerViews() {
        binding.ingredientsSection.ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
        }
        binding.instructionsSection.instructionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
        }
    }

    /**
     * Observes ViewModel data and updates UI accordingly.
     */
    private fun setupViewModelObservers() {
        viewModel.mealDetail.observe(viewLifecycleOwner) { meal ->
            meal?.let {
                currentMeal = meal
                setupFavoriteHandling()
                updateUIWithMealData(meal)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            binding.errorText.apply {
                text = errorMessage ?: ""
                visibility = if (errorMessage != null) View.VISIBLE else View.GONE
            }
        }
    }

    /**
     * Updates UI elements with meal data.
     */
    private fun updateUIWithMealData(meal: DetailedMeal) {
        binding.recipeTitle.text = meal.strMeal
        binding.recipeMeta.recipeCategory.text = meal.strCategory ?: ""
        binding.recipeMeta.recipeOrigin.text = meal.strArea ?: ""

        Glide.with(this)
            .load(meal.strMealThumb)
            .placeholder(R.drawable.placeholder_recipe)
            .into(binding.recipeImage)

        ingredientsAdapter = IngredientsAdapter(extractIngredients(meal))
        binding.ingredientsSection.ingredientsRecyclerView.adapter = ingredientsAdapter

        instructionsAdapter = InstructionsAdapter(extractInstructions(meal.strInstructions))
        binding.instructionsSection.instructionsRecyclerView.adapter = instructionsAdapter
    }

    /**
     * Handles user authentication and favorite functionality.
     */
    private fun setupUserAndFavoriteHandling() {
        currentUserId = SharedPrefsHelper.getUserId(requireContext())

        if (currentUserId == -1) {
            binding.favoriteButton.isEnabled = false
            binding.favoriteButton.setOnClickListener {
                Toast.makeText(context, "Please log in to favorite recipes", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Observe only the isFavorited flag
            favoritesViewModel.isFavorited.observe(viewLifecycleOwner) { favorited ->
                isFavorite = favorited
                updateFavoriteIcon()
            }

            // Fetch meal detail after user is verified
            viewModel.fetchMealDetail(args.recipeId)
        }
    }

    /**
     * Configures favorite button behavior with Toast notifications.
     */
    private fun setupFavoriteHandling() {
        val meal = currentMeal ?: return
        if (currentUserId == -1) return

        favoritesViewModel.checkIfFavorited(meal.idMeal, currentUserId)
        binding.favoriteButton.setOnClickListener {
            val favoriteRecipe = FavoriteRecipeEntity(
                idMeal = meal.idMeal,
                userId = currentUserId,
                mealName = meal.strMeal,
                mealThumb = meal.strMealThumb,
                category = meal.strCategory
            )
            if (isFavorite) {
                favoritesViewModel.removeFavoriteById(meal.idMeal, currentUserId)
                isFavorite = false
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
            } else {
                favoritesViewModel.addFavorite(favoriteRecipe)
                isFavorite = true
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
            }
            updateFavoriteIcon()
        }
    }

    /**
     * Updates the favorite button icon based on favorite status.
     */
    private fun updateFavoriteIcon() {
        binding.favoriteButton.setImageResource(
            if (isFavorite) R.drawable.favorite_filled else R.drawable.favorite
        )
    }

    /**
     * Extracts ingredients from meal data using reflection.
     */
    private fun extractIngredients(meal: DetailedMeal): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        for (i in 1..20) {
            val ingredientField = meal::class.java.getDeclaredField("strIngredient$i").apply { isAccessible = true }
            val measureField = meal::class.java.getDeclaredField("strMeasure$i").apply { isAccessible = true }
            val ingredient = ingredientField.get(meal) as? String
            val measure = measureField.get(meal) as? String
            if (!ingredient.isNullOrBlank() && ingredient != "null") {
                ingredients.add(Ingredient(ingredient.trim(), measure?.trim() ?: ""))
            }
        }
        return ingredients
    }

    /**
     * Splits instructions text into individual steps.
     */
    private fun extractInstructions(instructions: String?): List<InstructionStep> {
        return instructions
            ?.split(Regex("[\\r\\n]+|(?<!\\b(?:e\\.g|i\\.e|Dr|Mr|Mrs|vs|etc))\\.(\\s+|$)"))
            ?.mapNotNull { line ->
                val trimmed = line.trim()
                if (trimmed.isNotEmpty()) InstructionStep(trimmed) else null
            }
            ?: emptyList()
    }

    /**
     * Plays YouTube video in overlay.
     */
    private fun playVideo(videoId: String) {
        binding.videoOverlayInclude.videoOverlay.visibility = View.VISIBLE
        youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
        binding.videoOverlayInclude.btnCloseVideo.setOnClickListener {
            binding.videoOverlayInclude.videoOverlay.visibility = View.GONE
            youtubePlayerView.release()
        }
    }

    /**
     * Extracts YouTube video ID from URL.
     */
    private fun extractYoutubeVideoId(url: String): String {
        val regex = "(?<=v=|v/|embed/|youtu.be/)([a-zA-Z0-9_-]{11})".toRegex()
        return regex.find(url)?.value ?: ""
    }

    override fun onDestroyView() {
        youtubePlayerView.release()
        _binding = null
        super.onDestroyView()
    }
}