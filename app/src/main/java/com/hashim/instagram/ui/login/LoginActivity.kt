package com.hashim.instagram.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.hashim.instagram.databinding.ActivityLoginBinding
import com.hashim.instagram.di.component.ActivityComponent
import com.hashim.instagram.ui.base.BaseActivity
import com.hashim.instagram.ui.main.MainActivity
import com.hashim.instagram.ui.signup.SignupActivity
import com.hashim.instagram.utils.common.Status

class LoginActivity : BaseActivity<LoginViewModel>() {

    lateinit var binding: ActivityLoginBinding

    companion object{
        const val TAG = "LoginActivity"
    }

    override fun provideLayoutId(): View {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
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
            viewModel.doSignup()
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.launchMain.observe(this, {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        })

        viewModel.launchSignup.observe(this, {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, SignupActivity::class.java))
            }
        })

        viewModel.emailField.observe(this, {
            if(binding.etEmail.text.toString() != it){
                binding.etEmail.setText(it.toString())
            }
        })

        viewModel.emailValidation.observe(this, {
            when(it.status){
                Status.ERROR -> binding.layoutEmail.error = it.data?.run{getString(this)}
                else -> binding.layoutEmail.isErrorEnabled = false
            }
        })

        viewModel.passwordField.observe(this, {
            if(binding.etPassword.text.toString() != it){
                binding.etPassword.setText(it.toString())
            }
        })

        viewModel.passwordValidation.observe(this, {
            when(it.status){
                Status.ERROR -> binding.layoutPassword.error = it.data?.run{getString(this)}
                else -> binding.layoutPassword.isErrorEnabled = false
            }
        })

        viewModel.loggingIn.observe(this, {
            binding.pbLoading.visibility = if(it) View.VISIBLE else View.GONE
        })

    }
}
