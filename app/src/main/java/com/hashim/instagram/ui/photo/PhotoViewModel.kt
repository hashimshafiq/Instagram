package com.hashim.instagram.ui.photo

import androidx.lifecycle.MutableLiveData
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
import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.InputStream

class PhotoViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository,
    private val postRepository: PostRepository,
    @TempDirectory private val directory : File
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    override fun onCreate() {}

    private val user: User = userRepository.getCurrentUser()!! // should not be used without logged in user
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val post: MutableLiveData<Event<Post>> = MutableLiveData()

    fun onGalleryImageSelected(inputStream: InputStream) {
        loading.postValue(true)
        compositeDisposable.add(
            Single.fromCallable {
                    FileUtils.saveInputStreamToFile(
                        inputStream, directory, "gallery_img_temp", 500
                    )
                }
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        if (it != null) {
                            FileUtils.getImageSize(it)?.run {
                                uploadPhotoAndCreatePost(it, this)
                            }
                        } else {
                            loading.postValue(false)
                            messageStringId.postValue(Resource.error(R.string.try_again))

                        }
                    },
                    {
                        loading.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.try_again))
                    }
                )
        )
    }

    fun onCameraImageTaken(cameraImageProcessor: () -> String) {
        loading.postValue(true)
        compositeDisposable.add(
            Single.fromCallable { cameraImageProcessor() }
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        File(it).apply {
                            FileUtils.getImageSize(this)?.let { size ->
                                uploadPhotoAndCreatePost(this, size)
                            } ?: loading.postValue(false)
                        }
                    },
                    {
                        loading.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.try_again))
                    }
                )
        )
    }

    private fun uploadPhotoAndCreatePost(imageFile: File, imageSize: Pair<Int, Int>) {
        compositeDisposable.add(
            photoRepository.uploadPhoto(imageFile, user)
                .flatMap {
                    postRepository.createPost(it, imageSize.first, imageSize.second, user)
                }
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        loading.postValue(false)
                        post.postValue(Event(it))
                    },
                    {
                        handleNetworkError(it)
                        loading.postValue(false)
                    }
                )

        )
    }
}