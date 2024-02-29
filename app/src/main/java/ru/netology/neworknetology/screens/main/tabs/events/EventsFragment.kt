package ru.netology.neworknetology.screens.main.tabs.events

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentEventsBinding
import ru.netology.neworknetology.dto.Event
import ru.netology.neworknetology.model.OnInteractionListener
import ru.netology.neworknetology.utils.findTopNavController
import ru.netology.neworknetology.utils.observeEvent

@AndroidEntryPoint
class EventsFragment : Fragment(R.layout.fragment_events) {

    private lateinit var binding: FragmentEventsBinding
    private val viewModel by viewModels<EventsViewModel>()
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentEventsBinding.inflate(layoutInflater)

        activity?.title = getString(R.string.events_fragment_title)

        adapter = EventAdapter(object : OnInteractionListener {
            override fun onEventLike(event: Event) {
                viewModel.likeById(event)
            }

            override fun onEventRemove(event: Event) {
                viewModel.removeById(event)
            }

            override fun onEventEdit(event: Event) {
                TODO("Not yet implemented")
            }
        })


        binding.list.adapter = adapter


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collectLatest {
                    adapter.submitList(it)
                }
            }
        }

        binding.eventsSwipeRefresh.setOnRefreshListener {
            viewModel.getAllEvent()
        }

        binding.add.setOnClickListener {
            findTopNavController().navigate(R.id.createEventFragment)
        }

        observeState()
        observeForbiddenEvent()

        return binding.root
    }

    private fun observeState() = viewModel.dataState.observe(viewLifecycleOwner) {
        binding.eventsSwipeRefresh.isRefreshing = it.loading

        if (it.error) {
            Toast.makeText(requireContext(), "Произошла ошибка при отправке запроса", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeForbiddenEvent() = viewModel.showForbiddenToastEvent.observeEvent(viewLifecycleOwner) {
        Toast.makeText(requireContext(), "У вас нет прав для выполнения действия", Toast.LENGTH_SHORT).show()
    }

}