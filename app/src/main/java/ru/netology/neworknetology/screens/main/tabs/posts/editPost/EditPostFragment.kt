package ru.netology.neworknetology.screens.main.tabs.posts.editPost

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentEditPostBinding
import ru.netology.neworknetology.model.enums.AttachmentType
import ru.netology.neworknetology.screens.main.tabs.posts.createPost.ChangePostViewModel
import ru.netology.neworknetology.utils.AndroidUtils
import ru.netology.neworknetology.utils.AndroidUtils.focusAndShowKeyboard
import ru.netology.neworknetology.utils.load
@AndroidEntryPoint
class EditPostFragment : Fragment(R.layout.fragment_edit_post) {
    private lateinit var binding: FragmentEditPostBinding
    private val viewModel by viewModels<ChangePostViewModel>()
    var type: AttachmentType? = null

    private val args by navArgs<EditPostFragmentArgs>()

    private val pickPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                ImagePicker.RESULT_ERROR -> {
                    Snackbar.make(
                        binding.root,
                        ImagePicker.getError(it.data),
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                Activity.RESULT_OK -> {
                    val uri: Uri = it.data?.data!!
                    val stream = uri.let { stream ->
                        context?.contentResolver?.openInputStream(stream)
                    }

                    viewModel.changeMedia(uri, stream, type)
                }
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditPostBinding.inflate(inflater, container, false)

        activity?.title = getString(R.string.edit_post_fragment_title)

        getPostArgument()
        val editedPost = viewModel.getEditedPost()
        val attachment = editedPost?.attachment

        binding.editText.setText(editedPost?.content)

        binding.itemContainer.setOnClickListener {
            binding.editText.focusAndShowKeyboard()
        }

        binding.addPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.CAMERA)
                .createIntent(pickPhotoLauncher::launch)
            type = AttachmentType.IMAGE
        }

        binding.removePhoto.setOnClickListener {
            viewModel.changeMedia(null,null,null)
            viewModel.changeAttachmentPhoto("")
            binding.photoPreviewContainer.visibility = View.GONE
        }

        binding.addFile.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpeg",
                    )
                )
                .createIntent(pickPhotoLauncher::launch)
            type = AttachmentType.IMAGE
        }

        binding.addLocation.setOnClickListener {
            Toast.makeText(requireContext(), "Нужно добавить работу с картами", Toast.LENGTH_SHORT)
                .show()
        }

        binding.addParticipater.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Нужно добавить работу с участниками",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (attachment?.url != null && attachment.type == AttachmentType.IMAGE) {
            binding.photoPreview.load(attachment.url)
            binding.photoPreviewContainer.visibility = View.VISIBLE
        } else {
            binding.photoPreviewContainer.visibility = View.GONE
        }

        viewModel.mediaState.observe(viewLifecycleOwner) { mediaState ->
            if (mediaState == null) {
                binding.photoPreviewContainer.visibility = View.GONE
                binding.removePhoto.visibility = View.GONE
                return@observe
            }
            if (mediaState.type == AttachmentType.IMAGE) {
                binding.photoPreviewContainer.visibility = View.VISIBLE
                binding.removePhoto.visibility = View.VISIBLE
                binding.photoPreview.load(mediaState.uri.toString())
            } else {
                binding.photoPreviewContainer.visibility = View.GONE
                binding.removePhoto.visibility = View.GONE
            }
        }


        view?.setOnTouchListener { v, _ ->
            AndroidUtils.hideKeyboard(v)
            true
        }

        makeMenu()

        return binding.root
    }

    private fun makeMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_event, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.addEventMenu -> {
                        if (binding.editText.text.toString().isNotBlank()) {
                            viewModel.saveEvent(binding.editText.text.toString())
                            findNavController().navigateUp()
                            true
                        } else {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.error_empty_field), Snackbar.LENGTH_LONG
                            ).show()
                            false
                        }
                    }

                    else -> false
                }
        }, viewLifecycleOwner)
    }

    override fun onDestroy() {
        viewModel.clear()
        viewModel.changeMedia(null, null, null)
        super.onDestroy()
    }

    private fun getPostArgument() {
        viewModel.setEditedPost(args.currentPost)
    }
}