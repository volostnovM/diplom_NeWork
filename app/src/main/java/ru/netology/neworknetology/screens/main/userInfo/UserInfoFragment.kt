package ru.netology.neworknetology.screens.main.userInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentUserInfoBinding
import ru.netology.neworknetology.utils.findTopNavController

@AndroidEntryPoint
class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private lateinit var binding: FragmentUserInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserInfoBinding.inflate(
            inflater,
            container,
            false
        )

        activity?.title = getString(R.string.you)

        makeMenu()

        return binding.root
    }

    private fun makeMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.logout_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.logoutMenu -> {
                        //TODO добавить очистку sharedPreference для логаута
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
}