package ru.netology.neworknetology.screens.main.userInfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.navOptions
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentUserInfoBinding
import ru.netology.neworknetology.dto.User
import ru.netology.neworknetology.screens.main.userInfo.adapter.PagerAdapter
import ru.netology.neworknetology.utils.findTopNavController
import ru.netology.neworknetology.utils.load
import kotlin.properties.Delegates

@AndroidEntryPoint
class UserInfoFragment : Fragment(R.layout.fragment_user_info) {
    private val viewModel by viewModels<UserInfoViewModel>()
    private lateinit var binding: FragmentUserInfoBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var pager: ViewPager2
    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserInfoBinding.inflate(inflater, container, false)

        makeMenu()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = binding.tabLayout
        pager = binding.pager

        val userInfo: User? = requireArguments().getParcelable(USER_INFO) ?: null
        var userId by Delegates.notNull<Int>()
        if(userInfo != null) {
            activity?.title = userInfo.name
            binding.mainPhoto.load(userInfo.avatar.orEmpty())
            userId = userInfo.id
            pagerAdapter = PagerAdapter(this, userId, false)
        } else {
            activity?.title = getString(R.string.you)
            userId = viewModel.getCurrentUserId()
            pagerAdapter = if (userId == 0) {
                PagerAdapter(this, null, false)
            } else {
                PagerAdapter(this, userId, true)
            }
        }

        Log.d("picker","userInfo = ${userInfo}  id = ${userId}")
        binding.pager.adapter = pagerAdapter


        TabLayoutMediator(tabLayout, pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.wall)
                1 -> tab.text = getString(R.string.jobs)
            }
        }.attach()
    }

    private fun makeMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.logout_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.logoutMenu -> {
                        viewModel.clearSetting()
                        findTopNavController().navigate(R.id.signInFragment, null, navOptions {
                            popUpTo(R.id.tabsFragment) {
                                inclusive = true
                            }
                        })
                        true
                    }

                    else -> false
                }
        }, viewLifecycleOwner)
    }
    companion object {
        const val USER_INFO = "userInfo"
    }
}