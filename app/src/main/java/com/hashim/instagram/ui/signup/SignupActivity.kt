package com.hashim.instagram.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.hashim.instagram.databinding.ActivitySignupBinding
import com.hashim.instagram.di.component.ActivityComponent
import com.hashim.instagram.ui.base.BaseActivity
import com.hashim.instagram.ui.main.MainActivity
import com.hashim.instagram.utils.common.Status

class SignupActivity : BaseActivity<SignupViewModel>(){

    lateinit var binding: ActivitySignupBinding

    override fun provideLayoutId(): View {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {

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

        viewModel.fullNameField.observe(this, {
            if(binding.etName.text.toString() != it){
                viewModel.fullNameField.postValue(it)
            }
        })

        viewModel.nameValidation.observe(this, {
            when(it.status) {
                Status.ERROR -> binding.layoutName.error = it.data?.run { getString(it.data) }
                else -> binding.layoutName.isErrorEnabled = false
            }
        })

        viewModel.emailField.observe(this, {
            if(binding.etEmail.text.toString() != it){
                viewModel.emailField.postValue(it)
            }
        })

        viewModel.emailValidation.observe(this, {
            when(it.status) {
                Status.ERROR -> binding.layoutEmail.error = it.data?.run { getString(it.data) }
                else -> binding.layoutEmail.isErrorEnabled = false
            }
        })

        viewModel.passwordField.observe(this, {
            if(binding.etPassword.text.toString() != it){
                viewModel.passwordField.postValue(it)
            }
        })

        viewModel.passwordValidation.observe(this, {
            when(it.status) {
                Status.ERROR -> binding.layoutPassword.error = it.data?.run { getString(it.data) }
                else -> binding.layoutPassword.isErrorEnabled = false
            }
        })

        viewModel.launchMain.observe(this, {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext,MainActivity::class.java))
            }
        })

        viewModel.siginningUp.observe(this, Observer {
            binding.pbLoading.visibility = if(it) View.VISIBLE else View.GONE
        })

    }
}