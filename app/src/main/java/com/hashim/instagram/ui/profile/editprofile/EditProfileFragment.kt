package com.hashim.instagram.ui.profile.editprofile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hashim.instagram.R
import com.hashim.instagram.databinding.FragmentEditProfileBinding
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment
import com.hashim.instagram.utils.common.GlideHelper
import java.io.FileNotFoundException

class EditProfileFragment : BaseFragment<EditProfileViewModel>() {

    private var _binding: FragmentEditProfileBinding? = null

    private val binding get() = _binding!!

    companion object {
        const val RESULT_GALLERY_IMAGE_CODE = 1001
    }

    override fun provideLayoutId(): Int = R.layout.fragment_edit_profile

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
       fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {

        binding.ivProfilePhoto.setOnClickListener {
            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }.run {
                startActivityForResult(this, RESULT_GALLERY_IMAGE_CODE)
            }
        }


        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEmailChange(s.toString())
            }

        })

        binding.etBio.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onBioChange(s.toString())
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

        binding.ivTick.setOnClickListener{
            viewModel.doUpdate()
        }

        binding.ivClose.setOnClickListener {

        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.nameField.observe(this, {
            if(binding.etName.text.toString() != it){
                binding.etName.setText(it.toString())
            }
        })

        viewModel.bioField.observe(this, {
            if(binding.etBio.text.toString() != it){
                binding.etBio.setText(it.toString())
            }
        })

        viewModel.loading.observe(this, {
            binding.pbLoading.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.emailField.observe(this, {
            if(binding.etEmail.text.toString() != it){
                binding.etEmail.setText(it.toString())
            }
        })

        viewModel.profile.observe(this, {
            it?.run {
                val glideRequest = Glide
                    .with(binding.ivProfilePhoto.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                glideRequest.into(binding.ivProfilePhoto)
            }
        })

        viewModel.selectedProfile.observe(this, {
            val glideRequest = Glide
                .with(binding.ivProfilePhoto.context)
                .load(it)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

            glideRequest.into(binding.ivProfilePhoto)
        })
    }

    override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(reqCode, resultCode, intent)
        if (resultCode == RESULT_OK) {
            when (reqCode) {
                RESULT_GALLERY_IMAGE_CODE -> {
                    try {
                        intent?.data?.let {
                            activity?.contentResolver?.openInputStream(it)?.run {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
