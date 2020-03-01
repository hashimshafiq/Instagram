package com.mindorks.bootcamp.instagram.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.EventLog
import android.view.View
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.dummy.DummyActivity
import com.mindorks.bootcamp.instagram.ui.main.MainActivity
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.common.Status
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : BaseActivity<SignupViewModel>(){

    override fun provideLayoutId(): Int = R.layout.activity_signup

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {

        et_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPasswordChange(s.toString())
            }
        })

        et_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEmailChange(s.toString())
            }
        })

        et_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onNameChange(s.toString())
            }
        })

        bt_login.setOnClickListener {
            viewModel.doSignup()
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.fullNameField.observe(this, Observer {
            if(et_name.text.toString() != it){
                viewModel.fullNameField.postValue(it)
            }
        })

        viewModel.nameValidation.observe(this, Observer {
            when(it.status) {
                Status.ERROR -> layout_name.error = it.data?.run { getString(it.data) }
                else -> layout_name.isErrorEnabled = false
            }
        })

        viewModel.emailField.observe(this, Observer {
            if(et_email.text.toString() != it){
                viewModel.emailField.postValue(it)
            }
        })

        viewModel.emailValidation.observe(this, Observer {
            when(it.status) {
                Status.ERROR -> layout_email.error = it.data?.run { getString(it.data) }
                else -> layout_email.isErrorEnabled = false
            }
        })

        viewModel.passwordField.observe(this, Observer {
            if(et_password.text.toString() != it){
                viewModel.passwordField.postValue(it)
            }
        })

        viewModel.passwordValidation.observe(this, Observer {
            when(it.status) {
                Status.ERROR -> layout_password.error = it.data?.run { getString(it.data) }
                else -> layout_password.isErrorEnabled = false
            }
        })

        viewModel.launchMain.observe(this, Observer<Event<Map<String,String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext,MainActivity::class.java))
            }
        })

        viewModel.siginningUp.observe(this, Observer {
            pb_loading.visibility = if(it) View.VISIBLE else View.GONE
        })

    }
}