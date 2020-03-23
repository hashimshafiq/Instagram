package com.hashim.instagram.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.hashim.instagram.R
import com.hashim.instagram.di.component.ActivityComponent
import com.hashim.instagram.ui.base.BaseActivity
import com.hashim.instagram.ui.main.MainActivity
import com.hashim.instagram.ui.signup.SignupActivity
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.common.Status
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginViewModel>() {

    companion object{
        const val TAG = "LoginActivity"
    }

    override fun provideLayoutId(): Int = R.layout.activity_login

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        et_email.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEmailChange(s.toString())
            }

        })

        et_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPasswordChange(s.toString())
            }
        })

        bt_login.setOnClickListener{
            viewModel.doLogin()
        }

        tv_signup.setOnClickListener{
            viewModel.doSignup()
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.launchMain.observe(this, Observer<Event<Map<String, String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        })

        viewModel.launchSignup.observe(this, Observer<Event<Map<String,String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, SignupActivity::class.java))
            }
        })

        viewModel.emailField.observe(this, Observer {
            if(et_email.text.toString() != it){
                et_email.setText(it.toString())
            }
        })

        viewModel.emailValidation.observe(this, Observer {
            when(it.status){
                Status.ERROR -> layout_email.error = it.data?.run{getString(this)}
                else -> layout_email.isErrorEnabled = false
            }
        })

        viewModel.passwordField.observe(this, Observer {
            if(et_password.text.toString() != it){
                et_password.setText(it.toString())
            }
        })

        viewModel.passwordValidation.observe(this, Observer {
            when(it.status){
                Status.ERROR -> layout_password.error = it.data?.run{getString(this)}
                else -> layout_password.isErrorEnabled = false
            }
        })

        viewModel.loggingIn.observe(this, Observer {
            pb_loading.visibility = if(it) View.VISIBLE else View.GONE
        })

    }
}
