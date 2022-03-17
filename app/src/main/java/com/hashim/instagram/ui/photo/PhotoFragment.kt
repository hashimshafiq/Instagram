package com.hashim.instagram.ui.photo

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.hashim.instagram.R
import com.hashim.instagram.databinding.FragmentPhotoBinding
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment
import com.hashim.instagram.ui.main.MainSharedViewModel
import com.hashim.instagram.utils.common.Event
import com.mindorks.paracamera.Camera
import java.io.FileNotFoundException
import javax.inject.Inject


class PhotoFragment : BaseFragment<PhotoViewModel>() {

    companion object {
        const val RESULT_GALLERY_IMG = 1002
        const val TAG = "PhotoFragment"
        var SELECTED_IMG_URL = ""

        fun newInstance(): PhotoFragment {
            val args = Bundle()
            val fragment = PhotoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var camera: Camera

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    private var _binding : FragmentPhotoBinding? = null

    private val binding get() = _binding!!

    override fun provideLayoutId(): Int = R.layout.fragment_photo

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    @SuppressLint("CheckResult")
    override fun setupView(view: View) {

        _binding = FragmentPhotoBinding.bind(view)

        binding.btnCamera.setOnClickListener {
            try {
                camera.takePicture()
            }catch (e : Exception){
                e.printStackTrace()
            }
        }

        binding.btnGallery.setOnClickListener {
            Intent(Intent.ACTION_PICK)
                .apply {
                    type = "image/*"
                }.run {
                    startActivityForResult(this, RESULT_GALLERY_IMG)
                }
        }

    }



    override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(reqCode, resultCode, intent)
        if (resultCode == RESULT_OK) {
            when (reqCode) {
                Camera.REQUEST_TAKE_PHOTO -> {
                    viewModel.onCameraImageTaken { camera.cameraBitmapPath }
                }
                RESULT_GALLERY_IMG -> {
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


    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.post.observe(this) {
            it.getIfNotHandled()?.run {
                mainSharedViewModel.newPost.postValue(Event(this))
                mainSharedViewModel.onHomeRedirect()
            }
        }

        viewModel.messgae.observe(this){
            it.getIfNotHandled()?.let { resource ->
                mainSharedViewModel.showSnackBar(resource)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


