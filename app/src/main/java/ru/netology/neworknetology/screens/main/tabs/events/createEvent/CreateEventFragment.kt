package ru.netology.neworknetology.screens.main.tabs.events.createEvent

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentCreateNewEventBinding
import ru.netology.neworknetology.utils.AndroidUtils
import ru.netology.neworknetology.utils.AndroidUtils.focusAndShowKeyboard

class CreateEventFragment : Fragment(R.layout.fragment_create_new_event) {
    private lateinit var binding: FragmentCreateNewEventBinding
    private val bottomSheetNewEvent = BottomSheetNewEvent()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentCreateNewEventBinding.inflate(inflater, container, false)

        activity?.title = getString(R.string.create_event)


        binding.addDate.setOnClickListener {
            bottomSheetNewEvent.show(parentFragmentManager, BottomSheetNewEvent.TAG)
        }

        binding.itemContainer.setOnClickListener {
            binding.editText.focusAndShowKeyboard()
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
                        true
                    }

                    else -> false
                }
        }, viewLifecycleOwner)
    }
}