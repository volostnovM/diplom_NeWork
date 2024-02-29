package ru.netology.neworknetology.screens.main.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentEventsBinding
import ru.netology.neworknetology.databinding.FragmentSignInBinding
import ru.netology.neworknetology.databinding.FragmentSignUpBinding
import ru.netology.neworknetology.utils.AndroidUtils
import ru.netology.neworknetology.utils.observeEvent

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel by viewModels<SignUpViewModel>()

    private lateinit var binding: FragmentSignUpBinding

    private val args by navArgs<SignUpFragmentArgs>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignUpBinding.bind(view)
        binding.createAccountButton.setOnClickListener { onCreateAccountButtonPressed() }

        if (getLoginArgument() != null) {
            binding.loginEditText.setText(getLoginArgument())
        }

        viewModel.state.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it.loading

            if (it.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG).show()
                viewModel.clean()
            }
            if (it.success) {
                viewModel.clean()
            }
        }

        observeNavigateToTabsEvent()
        observeShowAuthErrorMessageEvent()

        view?.setOnTouchListener { v, _ ->
            AndroidUtils.hideKeyboard(v)
            true
        }

    }

    private fun onCreateAccountButtonPressed() {
        AndroidUtils.hideKeyboard(requireView())
        val login = binding.loginEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.repeatPasswordEditText.text.toString().trim()
        val name = binding.usernameEditText.text.toString()

        if (login.isBlank() || password.isBlank() || name.isBlank() || confirmPassword.isBlank()) {
            Snackbar.make(
                binding.root,
                getString(R.string.error_empty_field), Snackbar.LENGTH_LONG
            ).show()
        } else {
            if (password == confirmPassword) {
                viewModel.signUp(login, password, name)
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.error_confirm_password), Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun observeNavigateToTabsEvent() = viewModel.navigateToTabsEvent.observeEvent(viewLifecycleOwner) {
        findNavController().navigate(R.id.action_signUpFragment_to_tabsFragment)
    }

    private fun observeShowAuthErrorMessageEvent() = viewModel.showAuthToastEvent.observeEvent(viewLifecycleOwner) {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }

    private fun getLoginArgument(): String? = args.login
}