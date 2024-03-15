package ru.netology.neworknetology.screens.main.tabs.posts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentPostBinding
import ru.netology.neworknetology.dto.Post
import ru.netology.neworknetology.model.OnInteractionListenerForPost
import ru.netology.neworknetology.screens.main.tabs.TabsFragmentDirections
import ru.netology.neworknetology.screens.main.userInfo.UserInfoFragment
import ru.netology.neworknetology.utils.findTopNavController
import ru.netology.neworknetology.utils.observeEvent

@AndroidEntryPoint
class PostsFragment : Fragment(R.layout.fragment_post) {

    private lateinit var binding: FragmentPostBinding
    private val viewModel by viewModels<PostViewModel>()
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentPostBinding.inflate(inflater, container, false)

        val isCurrentUser = arguments?.getBoolean(IS_CURRENT_USER) ?: false
        val userId = arguments?.getInt(USER_ID) ?: 0

        Log.d("picker", "postCreate userId = ${userId}  isCurrentUser = ${isCurrentUser}")

        if(!isCurrentUser) {
            activity?.title = getString(R.string.posts_fragment_title)
        }

        binding.add.isVisible = userId == null
        binding.add.setOnClickListener {
            findTopNavController().navigate(R.id.createPostFragment)
        }

        adapter = PostAdapter(object : OnInteractionListenerForPost {
            override fun onPostLike(post: Post) {
                viewModel.likeById(post)
            }

            override fun onPostRemove(post: Post) {
                viewModel.removeById(post)
            }

            override fun onPostEdit(post: Post) {
                val direction = TabsFragmentDirections.actionTabsFragmentToEditPostFragment(post)
                findTopNavController().navigate(direction)
            }

            override fun onPostShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onImageClickEvent(post: Post) {
                post.attachment?.url.let {
                    val direction = TabsFragmentDirections.actionTabsFragmentToImageFragment(it)
                    findTopNavController().navigate(direction)
                }
            }

            override fun onAuthorClickEvent(post: Post) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.getUserById(post.authorId).join()
                }
            }
        })


        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collectLatest {
                    if (userId != null && userId != 0) {
                        adapter.submitList(it.filter { feedItem ->
                            feedItem is Post && feedItem.authorId == userId
                        })
                    } else {
                        adapter.submitList(it)
                    }

//                    adapter.submitList(it)
                }
            }
        }

        binding.postsSwipeRefresh.setOnRefreshListener {
            viewModel.getAllPost()
        }

        observeState()
        observeForbiddenEvent()

        observeNavigateToUserInfoEvent()

        return binding.root
    }

    private fun observeNavigateToUserInfoEvent()  = viewModel.navigateToUserInfoEvent.observeEvent(viewLifecycleOwner) {
        arguments = bundleOf(UserInfoFragment.USER_INFO to it)
        findTopNavController().navigate(R.id.userInfoFragment, arguments)
    }

    private fun observeState() = viewModel.dataState.observe(viewLifecycleOwner) {
        binding.postsSwipeRefresh.isRefreshing = it.loading

        if (it.error) {
            Toast.makeText(
                requireContext(),
                "Произошла ошибка при отправке запроса",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeForbiddenEvent() =
        viewModel.showForbiddenToastEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                "У вас нет прав для выполнения действия",
                Toast.LENGTH_SHORT
            ).show()
        }

    companion object {
        const val USER_ID = "userId"
        const val IS_CURRENT_USER = "isCurrentUser"
    }

}