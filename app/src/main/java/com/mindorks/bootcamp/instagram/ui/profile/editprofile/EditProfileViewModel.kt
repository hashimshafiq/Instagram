package com.mindorks.bootcamp.instagram.ui.profile.editprofile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.Image
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.remote.Networking
import com.mindorks.bootcamp.instagram.data.repository.PhotoRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.*
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.InputStream

class EditProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository,
    private val directory: File
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper){


    val nameField : MutableLiveData<String> = MutableLiveData()
    val bioField : MutableLiveData<String> = MutableLiveData()
    val emailField : MutableLiveData<String> = MutableLiveData()
    val loading : MutableLiveData<Boolean> = MutableLiveData()
    val profile : MutableLiveData<Image> = MutableLiveData()
    val selectedProfile : MutableLiveData<Bitmap> = MutableLiveData()
    private var isProfileImageChange : Boolean = false
    private var fileName : String = ""
    private var file : File? = null
    private val user: User = userRepository.getCurrentUser()!!


    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )

    fun onNameChange(name : String) = nameField.postValue(name)

    fun onBioChange(bio : String) = bioField.postValue(bio)

    fun onEmailChange(email : String) = emailField.postValue(email)

    override fun onCreate() {
        fetchProfileData()
    }

    fun doUpdate(){
        val name = nameField.value
        val bio = bioField.value

        if(isProfileImageChange){
            if(checkInternetConnectionWithMessage()){
                loading.postValue(true)
                compositeDisposable.add(
                    photoRepository.uploadPhoto(file!!,user).flatMap {
                            fileName = it
                            userRepository.doUserProfileUpdate(user,name,bio,fileName)
                        }.subscribeOn(schedulerProvider.io())
                        .subscribe(
                            {
                                loading.postValue(false)
                                userRepository.removeCurrentUser()
                                userRepository.saveCurrentUser(User(user.id,
                                    it.name!!,user.email,user.accessToken,fileName))
                            },
                            {
                                loading.postValue(false)
                                handleNetworkError(it)
                            }
                        )
                )
            }

        }else{
            if(checkInternetConnectionWithMessage()){
                loading.postValue(true)
                compositeDisposable.add(
                    userRepository.doUserProfileUpdate(user,name,bio,fileName)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(
                            {
                                loading.postValue(false)
                                userRepository.removeCurrentUser()
                                userRepository.saveCurrentUser(User(user.id,it.name!!,user.email,user.accessToken,fileName))
                            },
                            {
                                loading.postValue(false)
                                handleNetworkError(it)
                            }
                        )
                )
            }
        }




    }

    private fun fetchProfileData(){
        loading.postValue(true)
        compositeDisposable.add(
            userRepository.doUserProfileFetch(user)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        loading.postValue(false)
                        nameField.postValue(it.data?.name)
                        profile.postValue(it.data?.profilePicUrl?.run {
                            fileName = this
                            Image(this,headers)
                        })
                        emailField.postValue(user.email)
                        bioField.postValue(it.data?.tagLine)
                    },
                    {
                        loading.postValue(false)
                        handleNetworkError(it)
                    }
                )
        )

    }

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
                            loading.postValue(false)
                            selectedProfile.postValue(BitmapFactory.decodeFile(it.absolutePath))
                            isProfileImageChange = true
                            file = it
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




}
