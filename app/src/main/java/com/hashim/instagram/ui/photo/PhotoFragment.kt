package com.hashim.instagram.ui.photo

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity.RESULT_OK
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.databinding.FragmentPhotoBinding
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment
import com.hashim.instagram.ui.main.MainSharedViewModel
import com.hashim.instagram.ui.photo.gallery.GalleryAdapter
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.common.GridSpacingItemDecoration
import com.mindorks.paracamera.Camera
import pub.devrel.easypermissions.EasyPermissions
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
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
    lateinit var galleryAdapter: GalleryAdapter

    @Inject
    lateinit var gridSpacingItemDecoration: GridSpacingItemDecoration

    private var _binding : FragmentPhotoBinding? = null

    private val binding get() = _binding!!



    override fun provideLayoutId(): Int = R.layout.fragment_photo

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    @SuppressLint("CheckResult")
    override fun setupView(view: View) {

        _binding = FragmentPhotoBinding.bind(view)

        if (hasReadPermission()){
            viewModel.getFilePaths()
        }else{
            EasyPermissions.requestPermissions(
                this,
                requireActivity().getString(R.string.rationale_ask),
                RESULT_GALLERY_IMG,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        binding.ivCamera.setOnClickListener {
            try {
                camera.takePicture()
            }catch (e : Exception){
                e.printStackTrace()
            }
        }

        binding.tvSubmit.setOnClickListener {
            try {
                activity?.contentResolver?.openInputStream(Uri.parse("file://$SELECTED_IMG_URL"))?.run {
                        viewModel.onGalleryImageSelected(this)

                } ?: showMessage(R.string.try_again)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                showMessage(R.string.try_again)
            }

        }

        binding.spFolders.apply {
            adapter = arrayAdapter
        }

        binding.rvImages.apply {
            layoutManager = gridLayoutManager
            adapter = galleryAdapter

        }.addItemDecoration(gridSpacingItemDecoration)

        binding.spFolders.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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

        GalleryAdapter.RxBus.itemClickStream.subscribe {
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

    private fun hasReadPermission(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.post.observe(this, {
            it.getIfNotHandled()?.run {
                mainSharedViewModel.newPost.postValue(Event(this))
                mainSharedViewModel.onHomeRedirect()
            }

        })

        viewModel.directoriesList.observe(this, {
            arrayAdapter.addAll(it)
        })

        viewModel.imagesList.observe(this, {
            galleryAdapter.updateData(it)
        })

        viewModel.imageDetail.observe(this, {
            it?.run {


                val glideRequest = Glide
                    .with(requireContext())
                    .load(url)

                glideRequest.into(binding.ivMain)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    @TargetApi(Build.VERSION_CODES.Q)
//    private fun queryImages() {
//        var imageList = mutableListOf<Image>()
//
//        val projection = arrayOf(
//                MediaStore.Images.Media._ID,
//                MediaStore.Images.Media.DATE_TAKEN,
//                MediaStore.Images.Media.WIDTH,
//                MediaStore.Images.Media.HEIGHT,
//                MediaStore.Images.Media.RELATIVE_PATH
//            )
//
//            val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"
//
//            val selectionArgs = arrayOf(
//                dateToTimestamp(day = 1, month = 1, year = 2000).toString()
//            )
//
//            val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
//
//            requireActivity().contentResolver.query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                projection,
//                selection,
//                selectionArgs,
//                sortOrder
//            )?.use { cursor ->
//                imageList = addImagesFromCursor(cursor)
//            }
//
//        galleryAdapter.updateData(imageList)
//    }
//
//    @TargetApi(Build.VERSION_CODES.Q)
//    private fun addImagesFromCursor(cursor: Cursor): MutableList<Image> {
//        val images = mutableListOf<Image>()
//
//        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//        val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
//
//
//        while (cursor.moveToNext()) {
//
//            val id = cursor.getLong(idColumn)
//            val path = cursor.getString(pathColumn)
//            val absolutePath = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id).path!!
//
//            val image = Image(absolutePath, mapOf())
//            images += image
//
//        }
//        return images
//    }
//
//    @Suppress("SameParameterValue")
//    @SuppressLint("SimpleDateFormat")
//    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
//        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
//            formatter.parse("$day.$month.$year")?.time ?: 0
//        }
}


