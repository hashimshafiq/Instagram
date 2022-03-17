package com.hashim.instagram.ui.photo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hashim.instagram.R
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

    private val user: User = userRepository.getCurrentUser()!! // should not be used without logged in user
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val post: MutableLiveData<Event<Post>> = MutableLiveData()
    val messgae: MutableLiveData<Event<Resource<Int>>> = MutableLiveData()

    fun onGalleryImageSelected(inputStream: InputStream) {

            try {
                viewModelScope.launch(coroutineDispatchers.default()) {
                    loading.postValue(true)
                    val file =
                        FileUtils.saveInputStreamToFile(
                            inputStream,
                            directory,
                            "gallery_img_temp",
                            500
                        )

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

            } catch (ex: Exception) {
                messgae.postValue(Event(handleNetworkError(ex)))
            }

    }

    fun onCameraImageTaken(cameraImageProcessor: () -> String) {

        try {

            viewModelScope.launch(coroutineDispatchers.default()) {
                loading.postValue(true)
                val str = cameraImageProcessor()
                val file = File(str)
                val fileSize = FileUtils.getImageSize(file)

                uploadPhotoAndCreatePost(file, fileSize!!)

            }

        }catch (e: Exception){
            messgae.postValue(Event(handleNetworkError(e)))
        }

    }

    @OptIn(FlowPreview::class)
    private fun uploadPhotoAndCreatePost(imageFile: File, imageSize: Pair<Int, Int>?) {

        try {
            viewModelScope.launch(coroutineDispatchers.io()) {
                photoRepository.uploadPhoto(imageFile,user)
                    .flatMapMerge {
                        postRepository.createPost(it, imageSize!!.first, imageSize.second, user)
                    }.collect {
                        loading.postValue(false)
                        post.postValue(Event(it))
                    }

            }

        }catch (ex: Exception){
            messgae.postValue(Event(handleNetworkError(ex)))
        }


    }

}