package com.mindorks.bootcamp.instagram.ui.profile.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.data.model.Image
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.*
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class EditProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper){


    val nameField : MutableLiveData<String> = MutableLiveData()
    val bioField : MutableLiveData<String> = MutableLiveData()
    val emailField : MutableLiveData<String> = MutableLiveData()
    val loading : MutableLiveData<Boolean> = MutableLiveData()

    private val user: User = userRepository.getCurrentUser()!!


    fun onNameChange(name : String) = nameField.postValue(name)

    fun onBioChange(bio : String) = bioField.postValue(bio)

    fun onEmailChange(email : String) = emailField.postValue(email)

    override fun onCreate() {
        fetchProfileData()
    }

    fun doUpdate(){
        val name = nameField.value
        val bio = bioField.value
        if(checkInternetConnectionWithMessage()){
            loading.postValue(true)
            compositeDisposable.add(
                userRepository.doUserProfileUpdate(user,name,bio,"")
                    .subscribeOn(schedulerProvider.io())
                    .subscribe(
                        {
                            loading.postValue(false)
                            userRepository.removeCurrentUser()
                            userRepository.saveCurrentUser(User(user.id,it.name!!,user.email,user.accessToken,""))
                        },
                        {
                            loading.postValue(false)
                            handleNetworkError(it)
                        }
                    )
                )
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
//                        profile.postValue(it.data?.profilePicUrl?.run {
//                            Image(this,headers)
//                        })
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




}
