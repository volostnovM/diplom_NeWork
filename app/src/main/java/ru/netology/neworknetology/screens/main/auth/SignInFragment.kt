package ru.netology.neworknetology.screens.main.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.FragmentSignInBinding
import ru.netology.neworknetology.utils.AndroidUtils.hideKeyboard
import ru.netology.neworknetology.utils.observeEvent


@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val viewModel by viewModels<SignInViewModel>()

    private lateinit var binding: FragmentSignInBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignInBinding.bind(view)
        binding.signInButton.setOnClickListener { onSignInButtonPressed() }
        binding.signUpButton.setOnClickListener { onSignUpButtonPressed() }

        observeState()
        observeShowAuthErrorMessageEvent()
        observeClearPasswordEvent()
        observeNavigateToTabsEvent()

        view.setOnTouchListener { v, _ ->
            hideKeyboard(v)
            true
        }
    }

    private fun onSignInButtonPressed() {
        hideKeyboard(requireView())
        binding.passwordTextInput.error = null

        val login = binding.loginEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (login.isBlank() || password.isBlank()) {
            Snackbar.make(
                binding.root,
                R.string.error_empty_login_and_pass,
                Snackbar.LENGTH_LONG
            ).show()
            binding.passwordTextInput.error = getString(R.string.error_empty_login_and_pass)
        } else {
            viewModel.signIn(login, password)
        }
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) {
        binding.progressBar.isVisible = it.loading
        binding.signInButton.isEnabled = !it.loading

        if (it.error) {
            Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG).show()
            viewModel.clean()
        }
        if (it.success) {
            viewModel.clean()
        }
    }

    private fun observeClearPasswordEvent() = viewModel.clearPasswordEvent.observeEvent(viewLifecycleOwner) {
        binding.passwordEditText.text?.clear()
    }

    private fun observeShowAuthErrorMessageEvent() = viewModel.showAuthToastEvent.observeEvent(viewLifecycleOwner) {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }

    private fun observeNavigateToTabsEvent() = viewModel.navigateToTabsEvent.observeEvent(viewLifecycleOwner) {
        findNavController().navigate(R.id.action_signInFragment_to_tabsFragment)
    }

    private fun onSignUpButtonPressed() {
        hideKeyboard(requireView())
        val login = binding.loginEditText.text.toString()
        val loginArg = login.ifBlank { null }

        val direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment(loginArg)
        findNavController().navigate(direction)
    }

}