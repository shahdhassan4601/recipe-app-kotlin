package com.example.recipeapp.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.remote.model.DetailedMeal
import com.example.recipeapp.databinding.FragmentRecipeDetailBinding
import com.example.recipeapp.di.RepositoryProvider
import com.example.recipeapp.viewmodel.DetailViewModel
import com.example.recipeapp.viewmodel.DetailViewModelFactory
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback


class RecipeDetailFragment : Fragment() {

    private var _binding :FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var instructionsAdapter: InstructionsAdapter

    private lateinit var youtubePlayerView: YouTubePlayerView
    private lateinit var viewModel: DetailViewModel

    private var currentMeal: DetailedMeal? = null

    private var isIngredientsExpanded = false
    private var isInstructionsExpanded = false

    private val args: RecipeDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        youtubePlayerView = binding.videoOverlayInclude.youtubePlayerView
        lifecycle.addObserver(youtubePlayerView)

        // Setup logic
        val repository = RepositoryProvider.provideRecipeRepository()
        val factory = DetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        val recipeId = args.recipeId
        viewModel.fetchMealDetail(recipeId)
        observeViewModel()

        setupViews()
        setupCollapsibleSections()
        setupRecyclerViews()
    }

    // 1. setting up buttons
    private fun setupViews(){
        binding.backButton.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.favoriteButton.setOnClickListener{
            //selectFavIcon()
            //add to favorites
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

    // 2. setting up collapsible sections(ingredients - instructions)
    private fun setupCollapsibleSections() {
        binding.ingredientsSection.ingredientsRecyclerView.visibility = View.GONE
        binding.instructionsSection.instructionsRecyclerView.visibility = View.GONE

        binding.ingredientsSection.ingredientsToggleIcon.setImageResource(R.drawable.chevron_down)
        binding.instructionsSection.instructionsToggleIcon.setImageResource(R.drawable.chevron_down)

        isIngredientsExpanded = false
        isInstructionsExpanded = false

        // Ingredients toggle logic
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

        // Instructions toggle logic
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

    fun toggleVisibilityWithAnimation(view: View, expand: Boolean) {
        val animation = if (expand) {
            AlphaAnimation(0f, 1f)
        } else {
            AlphaAnimation(1f, 0f)
        }
        animation.duration = 300
        view.startAnimation(animation)
        view.visibility = if (expand) View.VISIBLE else View.GONE
    }


    //3. setting up recycler view
    private fun setupRecyclerViews() {
        //ingredients
        var ingredientslist: List<Ingredient> = listOf() //will be fetched
        ingredientsAdapter = IngredientsAdapter(ingredientslist)
        binding.ingredientsSection.ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ingredientsAdapter
            isNestedScrollingEnabled = false
        }
        //instructions
        var instructionslist: List<InstructionStep> = listOf() //will be fetched
        instructionsAdapter = InstructionsAdapter(instructionslist)
        binding.instructionsSection.instructionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = instructionsAdapter
            isNestedScrollingEnabled = false
        }
    }

    //4. populating data
    private fun observeViewModel() {
        viewModel.mealDetail.observe(viewLifecycleOwner) { meal ->
            meal?.let {
                currentMeal = meal

                // Basic UI
                binding.recipeTitle.text = meal.strMeal
                binding.recipeMeta.recipeCategory.text = meal.strCategory ?: ""
                binding.recipeMeta.recipeOrigin.text = meal.strArea ?: ""

                Glide.with(this)
                    .load(meal.strMealThumb)
                    .into(binding.recipeImage)

                // YouTube video (if present)
                meal.strYoutube?.let { url ->
                    val videoId = extractYoutubeVideoId(url)
                    binding.watchVideoButton.setOnClickListener {
                        playVideo(videoId)
                    }
                }

                // Populate ingredients + measures
                val ingredients = extractIngredients(meal)
                ingredientsAdapter.updateList(ingredients)

                // Populate instructions
                val instructions = extractInstructions(meal.strInstructions)
                instructionsAdapter.updateList(instructions)
            }
        }
    }

    private fun extractIngredients(meal: DetailedMeal): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()

        for (i in 1..20) {
            val ingredientField = meal::class.java.getDeclaredField("strIngredient$i").apply {
                isAccessible = true
            }
            val measureField = meal::class.java.getDeclaredField("strMeasure$i").apply {
                isAccessible = true
            }

            val ingredient = ingredientField.get(meal) as? String
            val measure = measureField.get(meal) as? String

            if (!ingredient.isNullOrBlank() && ingredient != "null") {
                ingredients.add(Ingredient(ingredient.trim(), measure?.trim() ?: ""))
            }
        }
        return ingredients
    }


    private fun extractInstructions(instructions: String?): List<InstructionStep> {
        return instructions
            ?.split(Regex("[\\r\\n]+|(?<!\\b(?:e\\.g|i\\.e|Dr|Mr|Mrs|vs|etc))\\.(\\s+|$)"))
            ?.mapNotNull { line ->
                val trimmed = line.trim()
                if (trimmed.isNotEmpty()) InstructionStep(trimmed) else null
            }
            ?: emptyList()
    }

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

    private fun extractYoutubeVideoId(url: String): String {
        return Uri.parse(url).getQueryParameter("v") ?: ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

