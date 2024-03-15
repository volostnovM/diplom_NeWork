package ru.netology.neworknetology.screens.main.userInfo.adapter

import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.netology.neworknetology.screens.main.tabs.posts.PostsFragment
import ru.netology.neworknetology.screens.main.userInfo.job.JobsFragment

class PagerAdapter (fragment: Fragment, private val userId: Int?, private val isCurrentUser: Boolean) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                Log.d("picker", "PagerAdapter userId = ${userId}")
                PostsFragment().apply {
                    arguments = bundleOf(PostsFragment.IS_CURRENT_USER to isCurrentUser)
                    arguments = bundleOf(PostsFragment.USER_ID to userId)
                }
            }

            1 -> {
                JobsFragment().apply {
                    arguments = bundleOf(JobsFragment.USER_ID to userId)
                }
            }

            else -> {
                error("Unknown position")
            }
        }
    }
}