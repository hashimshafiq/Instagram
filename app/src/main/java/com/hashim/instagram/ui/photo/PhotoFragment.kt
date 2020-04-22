package com.hashim.instagram.ui.photo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.hashim.instagram.R
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment
import com.hashim.instagram.ui.main.MainSharedViewModel
import com.hashim.instagram.ui.photo.images.ImagesAdapter
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.common.GridSpacingItemDecoration
import com.mindorks.paracamera.Camera
import kotlinx.android.synthetic.main.fragment_photo.*
import pub.devrel.easypermissions.EasyPermissions
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

    @Inject
    lateinit var gridLayoutManager: GridLayoutManager

    @Inject
    lateinit var arrayAdapter: ArrayAdapter<String>

    @Inject
    lateinit var imagesAdapter: ImagesAdapter

    @Inject
    lateinit var gridSpacingItemDecoration: GridSpacingItemDecoration

    override fun provideLayoutId(): Int = R.layout.fragment_photo

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    @SuppressLint("CheckResult")
    override fun setupView(view: View) {

        if (hasWritePermission()){
            viewModel.getFilePaths()
        }else{
            EasyPermissions.requestPermissions(
                this,
                requireActivity().getString(R.string.rationale_ask),
                RESULT_GALLERY_IMG,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        ivCamera.setOnClickListener {
            try {
                camera.takePicture()
            }catch (e : Exception){
                e.printStackTrace()
            }
        }

        tvSubmit.setOnClickListener {
            try {
                activity?.contentResolver?.openInputStream(Uri.parse("file://$SELECTED_IMG_URL"))?.run {
                        viewModel.onGalleryImageSelected(this)

                } ?: showMessage(R.string.try_again)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                showMessage(R.string.try_again)
            }

        }

        spFolders.apply {
            adapter = arrayAdapter
        }

        rvImages.apply {
            layoutManager = gridLayoutManager
            adapter = imagesAdapter

        }.addItemDecoration(gridSpacingItemDecoration)

        spFolders.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.getImagePaths(id.toInt())
            }

        }

        ImagesAdapter.RxBus.itemClickStream.subscribe {
            viewModel.fetchDetailedImage(it)
        }

    }



    override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(reqCode, resultCode, intent)
        if (resultCode == RESULT_OK) {
            when (reqCode) {
                Camera.REQUEST_TAKE_PHOTO -> {
                    viewModel.onCameraImageTaken { camera.cameraBitmapPath }
                }

            }
        }
    }

    private fun hasWritePermission(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            pb_loading.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.post.observe(this, Observer {
            it.getIfNotHandled()?.run {
                mainSharedViewModel.newPost.postValue(Event(this))
                mainSharedViewModel.onHomeRedirect()
            }

        })

        viewModel.directoriesList.observe(this, Observer {
            arrayAdapter.addAll(it)
        })

        viewModel.imagesList.observe(this, Observer {
            imagesAdapter.updateData(it)
        })

        viewModel.imageDetail.observe(this, Observer {
            it?.run {


                val glideRequest = Glide
                    .with(requireContext())
                    .load(url)

                glideRequest.into(ivMain)
                SELECTED_IMG_URL = url
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

}


