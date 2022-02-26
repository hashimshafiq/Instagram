package com.hashim.instagram.ui.signup

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hashim.instagram.R
import com.hashim.instagram.databinding.FragmentSignupBinding
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment
import com.hashim.instagram.utils.common.Status

class SignupFragment : BaseFragment<SignupViewModel>(){

    private var _binding: FragmentSignupBinding? = null

    private val binding get() = _binding!!

    override fun provideLayoutId(): Int = R.layout.fragment_signup

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {

        _binding = FragmentSignupBinding.bind(view)

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPasswordChange(s.toString())
            }
        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEmailChange(s.toString())
            }
        })

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onNameChange(s.toString())
            }
        })

        binding.btLogin.setOnClickListener {
            viewModel.doSignup()
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.fullNameField.observe(this) {
            if (binding.etName.text.toString() != it) {
                viewModel.fullNameField.postValue(it)
            }
        }

        viewModel.nameValidation.observe(this) {
            when (it.status) {
                Status.ERROR -> binding.layoutName.error = it.data?.run { getString(it.data) }
                else -> binding.layoutName.isErrorEnabled = false
            }
        }

        viewModel.emailField.observe(this) {
            if (binding.etEmail.text.toString() != it) {
                viewModel.emailField.postValue(it)
            }
        }

        viewModel.emailValidation.observe(this) {
            when (it.status) {
                Status.ERROR -> binding.layoutEmail.error = it.data?.run { getString(it.data) }
                else -> binding.layoutEmail.isErrorEnabled = false
            }
        }

        viewModel.passwordField.observe(this) {
            if (binding.etPassword.text.toString() != it) {
                viewModel.passwordField.postValue(it)
            }
        }

        viewModel.passwordValidation.observe(this) {
            when (it.status) {
                Status.ERROR -> binding.layoutPassword.error = it.data?.run { getString(it.data) }
                else -> binding.layoutPassword.isErrorEnabled = false
            }
        }

        viewModel.launchMain.observe(this) {
            it.getIfNotHandled()?.run {
                findNavController().navigate(R.id.action_signupFragment_to_itemHome)
            }
        }

        viewModel.siginningUp.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}