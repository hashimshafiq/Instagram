package com.hashim.instagram.ui.photo

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.repository.PhotoRepository
import com.hashim.instagram.data.repository.PostRepository
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.di.TempDirectory
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.common.FileUtils
import com.hashim.instagram.utils.common.Resource
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

class PhotoViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository,
    private val postRepository: PostRepository,
    @TempDirectory private val directory : File
) : BaseViewModel(coroutineDispatchers, networkHelper) {

    override fun onCreate() {}

    private var directries: ArrayList<String> = ArrayList()
    private var imgageUrls : ArrayList<Image>? = ArrayList()
    private var directoryNames : ArrayList<String> = ArrayList()
    private val user: User = userRepository.getCurrentUser()!! // should not be used without logged in user
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val post: MutableLiveData<Event<Post>> = MutableLiveData()
    val directoriesList: MutableLiveData<List<String>> = MutableLiveData()
    val imagesList: MutableLiveData<List<Image>> = MutableLiveData()
    val imageDetail : MutableLiveData<Image> = MutableLiveData()

    fun onGalleryImageSelected(inputStream: InputStream) {

        viewModelScope.launch(coroutineDispatchers.default()) {
            loading.postValue(true)
            val file =
                FileUtils.saveInputStreamToFile(inputStream, directory, "gallery_img_temp", 500)

            if (file != null) {

                    uploadPhotoAndCreatePost(
                        file,
                        FileUtils.getImageSize(file)!!
                    )


            } else {
                loading.postValue(false)
                messageStringId.postValue(Resource.error(R.string.try_again))

            }
        }
    }

    fun onCameraImageTaken(cameraImageProcessor: () -> String) {

        viewModelScope.launch(coroutineDispatchers.default()) {
            loading.postValue(true)
            val str = cameraImageProcessor()
            val file = File(str)
            val fileSize = FileUtils.getImageSize(file)

            uploadPhotoAndCreatePost(file, fileSize!!)

        }
    }

    @OptIn(FlowPreview::class)
    private fun uploadPhotoAndCreatePost(imageFile: File, imageSize: Pair<Int, Int>?) {

        viewModelScope.launch(coroutineDispatchers.io()) {
            photoRepository.uploadPhoto(imageFile,user)
                .flatMapMerge {
                    postRepository.createPost(it, imageSize!!.first, imageSize.second, user)
                }.collect {
                    loading.postValue(false)
                    post.postValue(Event(it))
                }

        }
    }

    fun getFilePaths() {
        directries = FileUtils.getDirectoryPaths(Environment.getExternalStorageDirectory().absoluteFile)
        for (i in directries){
            directoryNames.add(i.substring(i.lastIndexOf("/")+1))
        }

        directoriesList.value = directoryNames
    }


    fun getImagePaths(index : Int){

        imgageUrls = FileUtils.getFilePaths(directries?.get(index))?.map {
            Image(it, mapOf())
        } as ArrayList<Image>?

        imagesList.value = imgageUrls

    }

    fun fetchDetailedImage(it: String) {
        imageDetail.value = Image(it, mapOf())
    }
}