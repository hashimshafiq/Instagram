package com.hashim.instagram.ui.login

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hashim.instagram.R
import com.hashim.instagram.databinding.FragmentLoginBinding
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment
import com.hashim.instagram.utils.common.Status

class LoginFragment : BaseFragment<LoginViewModel>() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    companion object{
        const val TAG = "LoginActivity"
    }

    override fun provideLayoutId(): Int  = R.layout.fragment_login


    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {

        _binding = FragmentLoginBinding.bind(view)

        binding.etEmail.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEmailChange(s.toString())
            }

        })


        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPasswordChange(s.toString())
            }
        })

        binding.btLogin.setOnClickListener{
            viewModel.doLogin()
        }

        binding.tvSignup.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.launchMain.observe(this) {
            it.getIfNotHandled()?.run {
                findNavController().navigate(R.id.action_loginFragment_to_itemHome)
            }
        }


        viewModel.emailField.observe(this) {
            if (binding.etEmail.text.toString() != it) {
                binding.etEmail.setText(it.toString())
            }
        }

        viewModel.emailValidation.observe(this) {
            when (it.status) {
                Status.ERROR -> binding.layoutEmail.error = it.data?.run { getString(this) }
                else -> binding.layoutEmail.isErrorEnabled = false
            }
        }

        viewModel.passwordField.observe(this) {
            if (binding.etPassword.text.toString() != it) {
                binding.etPassword.setText(it.toString())
            }
        }

        viewModel.passwordValidation.observe(this) {
            when (it.status) {
                Status.ERROR -> binding.layoutPassword.error = it.data?.run { getString(this) }
                else -> binding.layoutPassword.isErrorEnabled = false
            }
        }

        viewModel.loggingIn.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
