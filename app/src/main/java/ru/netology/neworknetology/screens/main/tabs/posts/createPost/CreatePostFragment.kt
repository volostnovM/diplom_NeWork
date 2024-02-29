package ru.netology.neworknetology.screens.main.tabs.posts.createPost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentCreateNewPostBinding
import ru.netology.neworknetology.databinding.FragmentPostBinding

@AndroidEntryPoint
class CreatePostFragment : Fragment(R.layout.fragment_create_new_post) {

    private lateinit var binding: FragmentCreateNewPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentCreateNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        activity?.title = getString(R.string.create_post_fragment_title)


        return binding.root
    }

}