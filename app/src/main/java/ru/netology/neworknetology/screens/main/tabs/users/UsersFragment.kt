package ru.netology.neworknetology.screens.main.tabs.users

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentUsersBinding
import ru.netology.neworknetology.screens.main.tabs.posts.PostViewModel

@AndroidEntryPoint
class UsersFragment : Fragment(R.layout.fragment_users) {

    private lateinit var binding: FragmentUsersBinding
    private lateinit var adapter: UsersAdapter
    private val viewModel by viewModels<UsersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentUsersBinding.inflate(layoutInflater)

        activity?.title = getString(R.string.users_fragment_title)

        adapter = UsersAdapter()
        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collectLatest {
                    adapter.submitList(it)
                }
            }
        }

        binding.usersSwipeRefresh.setOnRefreshListener {
            viewModel.getUserList()
        }

        observeState()

        return binding.root
    }

    private fun observeState() = viewModel.dataState.observe(viewLifecycleOwner) {
        binding.usersSwipeRefresh.isRefreshing = it.loading
    }
}