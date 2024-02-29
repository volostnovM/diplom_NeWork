package ru.netology.neworknetology.screens.main.tabs.posts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentEventsBinding
import ru.netology.neworknetology.databinding.FragmentPostBinding
import ru.netology.neworknetology.databinding.FragmentSignUpBinding
import ru.netology.neworknetology.screens.main.auth.SignInViewModel
import ru.netology.neworknetology.utils.findTopNavController

@AndroidEntryPoint
class PostsFragment : Fragment(R.layout.fragment_post) {

    private lateinit var binding: FragmentPostBinding
    private val viewModel by viewModels<PostViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentPostBinding.inflate(
            inflater,
            container,
            false
        )

        activity?.title = getString(R.string.posts_fragment_title)

        binding.add.setOnClickListener {
            findTopNavController().navigate(R.id.createPostFragment)
        }

        return binding.root
    }



}