package ru.netology.neworknetology.screens.main.tabs.events.createEvent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.netology.neworknetology.databinding.BottomSheetDatePickerBinding

class BottomSheetNewEvent : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDatePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDatePickerBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}