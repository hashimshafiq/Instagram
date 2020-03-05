package com.mindorks.bootcamp.instagram.ui.profile.editprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : BaseActivity<EditProfileViewModel>() {

    override fun provideLayoutId(): Int = R.layout.activity_edit_profile

    override fun injectDependencies(activityComponent: ActivityComponent) {
       activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {


        et_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEmailChange(s.toString())
            }

        })

        et_bio.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onBioChange(s.toString())
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

        ivTick.setOnClickListener{
            viewModel.doUpdate()
        }

        ivClose.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.nameField.observe(this, Observer {
            if(et_name.text.toString() != it){
                et_name.setText(it.toString())
            }
        })

        viewModel.bioField.observe(this, Observer {
            if(et_bio.text.toString() != it){
                et_bio.setText(it.toString())
            }
        })

        viewModel.loading.observe(this, Observer {
            pb_loading.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.emailField.observe(this, Observer {
            if(et_email.text.toString() != it){
                et_email.setText(it.toString())
            }
        })
    }
}
