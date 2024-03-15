package ru.netology.neworknetology.screens.main.tabs.events.createEvent

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.neworknetology.databinding.BottomSheetDatePickerBinding
import ru.netology.neworknetology.model.enums.EventType
import ru.netology.neworknetology.utils.AndroidUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class BottomSheetNewEvent : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDatePickerBinding
    private val viewModel by viewModels<ChangeEventViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDatePickerBinding.inflate(inflater, container, false)

        binding.radioButtonOnline.isChecked = true

        binding.radioButtonGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioButtonOnline.id -> viewModel.saveDraftType(EventType.ONLINE)
                binding.radioButtonOffline.id -> viewModel.saveDraftType(EventType.OFFLINE)
            }
        }

        binding.dateInputLayout.editText?.setOnClickListener {
            showTimePicker(requireContext())
            showDatePicker(requireContext())
        }


        view?.setOnTouchListener { v, _ ->
            AndroidUtils.hideKeyboard(v)
            true
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(context: Context) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(calendar.time)
                viewModel.saveDraftDate(formattedDate.toString())
                binding.dateInputLayout.editText?.setText(viewModel.getDraftDate() + " " + viewModel.getDraftTime())
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker(context: Context) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar[Calendar.HOUR_OF_DAY] = hour
            calendar[Calendar.MINUTE] = minute
            val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
            viewModel.saveDraftTime(formattedTime.toString())
        }
        TimePickerDialog(
            context, timePickerDialog, calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), true
        ).show()
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }
}