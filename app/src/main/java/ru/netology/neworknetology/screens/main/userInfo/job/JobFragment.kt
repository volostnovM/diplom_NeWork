package ru.netology.neworknetology.screens.main.userInfo.job

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentJobBinding
import ru.netology.neworknetology.databinding.JobCardBinding
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class JobsFragment : Fragment(R.layout.fragment_job) {
    private val viewModel by viewModels<JobViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentJobBinding.inflate(inflater, container, false)

        val userId = arguments?.getInt(USER_ID)
        Log.d("picker", "JobsFragment userId=${userId}")

        viewModel.getJobs(userId)

        binding.buttonNewJob.isVisible = userId == null || userId == viewModel.getCurrentUserId()

        viewModel.data.observe(viewLifecycleOwner) { jobs ->
            binding.containerJob.removeAllViews()
            jobs.forEach { job ->
                JobCardBinding.inflate(layoutInflater, binding.containerJob, true).apply {
                    companyName.text = job.name
                    timeWork.text = buildString {
                        append(job.start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                        append(" - ")
                        append(
                            if (job.finish.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) == "1970-01-01T00:00:00Z") {
                                getString(R.string.present_time)
                            } else {
                                job.finish.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                            }
                        )
                    }
                    speciality.text = job.position

                    buttonRemoveJob.isVisible = userId == null || userId == viewModel.getCurrentUserId()
                    buttonRemoveJob.setOnClickListener {
                        viewModel.deleteJob(job.id)
                    }
                }.root
            }
        }

        binding.buttonNewJob.setOnClickListener {
            findNavController().navigate(R.id.action_userInfoFragment_to_createJobFragment)
        }

        return binding.root
    }


    companion object {
        fun newInstance() = JobsFragment()
        const val USER_ID = "userId"
    }
}
