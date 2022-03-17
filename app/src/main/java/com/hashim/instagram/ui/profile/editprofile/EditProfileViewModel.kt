package com.hashim.instagram.ui.profile.editprofile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.Networking
import com.hashim.instagram.data.repository.PhotoRepository
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.*
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

class EditProfileViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository,
    private val directory: File
) : BaseViewModel(coroutineDispatchers, networkHelper){


    val nameField : MutableLiveData<String> = MutableLiveData()
    val bioField : MutableLiveData<String> = MutableLiveData()
    val emailField : MutableLiveData<String> = MutableLiveData()
    val loading : MutableLiveData<Boolean> = MutableLiveData()
    val profile : MutableLiveData<Image> = MutableLiveData()
    val selectedProfile : MutableLiveData<Bitmap> = MutableLiveData()
    val message: MutableLiveData<Event<Resource<Int>>> = MutableLiveData()

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

    @OptIn(FlowPreview::class)
    fun doUpdate(){
        val name = nameField.value
        val bio = bioField.value

        viewModelScope.launch(coroutineDispatchers.io()) {

            if(isProfileImageChange){
                if(checkInternetConnectionWithMessage()){

                    try {
                        photoRepository.uploadPhoto(file!!,user).flatMapMerge {
                            fileName = it
                            userRepository.doUserProfileUpdate(user,name,bio,fileName)
                        }.onStart {
                            loading.postValue(true)
                        }.collect {
                            loading.postValue(false)
                            userRepository.removeCurrentUser()
                            userRepository.saveCurrentUser(User(user.id,
                                it.name!!,user.email,user.accessToken,fileName))
                        }
                    }catch (ex: Exception){
                        loading.postValue(false)
                        message.postValue(Event(handleNetworkError(ex)))
                    }

                }

            }else {


                    try {
                        userRepository.doUserProfileUpdate(user, name, bio, fileName)
                            .onStart { loading.postValue(true) }
                            .collect {
                                loading.postValue(false)
                                userRepository.removeCurrentUser()
                                userRepository.saveCurrentUser(
                                    User(
                                        user.id,
                                        it.name!!,
                                        user.email,
                                        user.accessToken,
                                        fileName
                                    )
                                )
                            }
                    } catch (ex: java.lang.Exception) {
                        loading.postValue(false)
                        message.postValue(Event(handleNetworkError(ex)))
                    }

            }
        }
    }

    private fun fetchProfileData(){

        viewModelScope.launch(coroutineDispatchers.io()) {

            try {
                userRepository.doUserProfileFetch(user)
                    .onStart { loading.postValue(true)  }
                    .collect{
                        loading.postValue(false)
                        nameField.postValue(it.data?.name)
                        profile.postValue(it.data?.profilePicUrl?.run {
                            fileName = this
                            Image(this,headers)
                        })
                        emailField.postValue(user.email)
                        bioField.postValue(it.data?.tagLine)
                    }
            }catch (ex: Exception){
                loading.postValue(false)
                message.postValue(Event(handleNetworkError(ex)))
            }
        }
    }

    fun onGalleryImageSelected(inputStream: InputStream) {

        viewModelScope.launch(coroutineDispatchers.default()) {

            try {
                loading.postValue(true)
                val f = FileUtils.saveInputStreamToFile(
                    inputStream, directory, "gallery_img_temp", 500)

                if (f != null) {
                    loading.postValue(false)
                    selectedProfile.postValue(BitmapFactory.decodeFile(f.absolutePath))
                    isProfileImageChange = true
                    file = f
                } else {
                    loading.postValue(false)
                    messageStringId.postValue(Resource.error(R.string.try_again))
                }
            }catch (ex: Exception){
                loading.postValue(false)
                messageStringId.postValue(Resource.error(R.string.try_again))
            }
        }
    }
}
