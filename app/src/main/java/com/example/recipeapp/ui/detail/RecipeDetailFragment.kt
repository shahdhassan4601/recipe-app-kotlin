package com.example.recipeapp.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRecipeDetailBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback


class RecipeDetailFragment : Fragment() {

    private var _binding :FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var instructionsAdapter: InstructionsAdapter

    private lateinit var youtubePlayerView: YouTubePlayerView

    //private var recipe: Recipe? = null

    private var isIngredientsExpanded = false
    private var isInstructionsExpanded = false

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

        setupViews()
        setupCollapsibleSections()
        setupRecyclerViews()
        populateData()
    }

    // 1. setting up buttons
    private fun setupViews(){
        binding.backButton.setOnClickListener{
            //nav to home
        }

        binding.favoriteButton.setOnClickListener{
            //selectFavIcon()
            //add to favorites
        }

        binding.watchVideoButton.setOnClickListener {
            val videoUrl = "https://www.youtube.com/watch?v=XXXX" // replace with MealDB value
            val videoId = extractYoutubeVideoId(videoUrl)
            playVideo(videoId)
        }
    }

    // 2. setting up collapsible sections(ingredients - instructions)
    private fun setupCollapsibleSections() {

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

    //4. fetch data from api
    private fun populateData(){


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
        youtubePlayerView.release()
        _binding = null
    }

}

