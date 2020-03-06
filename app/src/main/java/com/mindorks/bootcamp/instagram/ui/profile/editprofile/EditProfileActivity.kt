package com.mindorks.bootcamp.instagram.ui.profile.editprofile

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.utils.common.GlideHelper
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.FileNotFoundException

class EditProfileActivity : BaseActivity<EditProfileViewModel>() {

    companion object {
        const val RESULT_GALLERY_IMAGE_CODE = 1001
    }

    override fun provideLayoutId(): Int = R.layout.activity_edit_profile

    override fun injectDependencies(activityComponent: ActivityComponent) {
       activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {

        ivProfilePhoto.setOnClickListener {
            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }.run {
                startActivityForResult(this, RESULT_GALLERY_IMAGE_CODE)
            }
        }


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

        viewModel.profile.observe(this, Observer {
            it?.run {
                val glideRequest = Glide
                    .with(ivProfilePhoto.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                glideRequest.into(ivProfilePhoto)
            }
        })

        viewModel.selectedProfile.observe(this, Observer {
            val glideRequest = Glide
                .with(ivProfilePhoto.context)
                .load(it)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

            glideRequest.into(ivProfilePhoto)
        })
    }

    override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(reqCode, resultCode, intent)
        if (resultCode == RESULT_OK) {
            when (reqCode) {
                RESULT_GALLERY_IMAGE_CODE -> {
                    try {
                        intent?.data?.let {
                            contentResolver?.openInputStream(it)?.run {
                                viewModel.onGalleryImageSelected(this)
                            }
                        } ?: showMessage(R.string.try_again)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        showMessage(R.string.try_again)
                    }
                }
            }
        }
    }
}
