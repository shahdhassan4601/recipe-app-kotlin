package com.example.recipeapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.recipeapp.databinding.FragmentSplashBinding
import com.example.recipeapp.data.prefs.SharedPrefsHelper
import com.example.recipeapp.ui.main.RecipeActivity
import com.example.recipeapp.R
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animateProgressBarAndNavigate()
    }

    private fun animateProgressBarAndNavigate() {
        lifecycleScope.launch {
            for (i in 0..100) {
                binding.loadingBar.progress = i+10
                delay(20) // total of ~2 seconds
            }

            // Optionally pause or cancel the Lottie after progress finishes
            binding.splashLottie.cancelAnimation()

            // After progress completes, check login and navigate
            val isLoggedIn = SharedPrefsHelper.isLoggedIn(requireContext())

            if (isLoggedIn) {
                startActivity(Intent(requireContext(), RecipeActivity::class.java))
                requireActivity().finish()
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}