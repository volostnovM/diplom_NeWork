package ru.netology.neworknetology.screens.main.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentImageBinding
import ru.netology.neworknetology.utils.load

@AndroidEntryPoint
class ImageFragment : Fragment(R.layout.fragment_image) {

    private lateinit var binding: FragmentImageBinding
    private val args by navArgs<ImageFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentImageBinding.inflate(inflater, container, false)
        val url = requireNotNull(getURLPictureArgument())
        binding.photo.load(url)

        return binding.root
    }


    private fun getURLPictureArgument() = args.url

}