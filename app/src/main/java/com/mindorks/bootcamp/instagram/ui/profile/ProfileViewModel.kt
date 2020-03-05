package com.mindorks.bootcamp.instagram.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.data.model.Image
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.remote.Networking
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class ProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    override fun onCreate() {
        fetchProfileData()
    }

    val name : MutableLiveData<String> = MutableLiveData()
    val profile : MutableLiveData<Image> = MutableLiveData()
    val tagLine : MutableLiveData<String> = MutableLiveData()
    val loading : MutableLiveData<Boolean> = MutableLiveData()
    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchEditProfile: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()


    private val user : User = userRepository.getCurrentUser()!!

    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )

    private fun fetchProfileData(){
        loading.postValue(true)
        compositeDisposable.add(
            userRepository.doUserProfileFetch(user)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        loading.postValue(false)
                        name.postValue(it.data?.name)
                        profile.postValue(it.data?.profilePicUrl?.run {
                            Image(this,headers)
                        })
                        tagLine.postValue(it.data?.tagLine)
                    },
                    {
                        loading.postValue(false)
                        handleNetworkError(it)
                    }
                )
        )

    }

    fun doLogout(){
        loading.postValue(true)
        compositeDisposable.add(
            userRepository.doLogout(user)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    loading.postValue(false)
                    userRepository.removeCurrentUser()
                    launchLogin.postValue(Event(mapOf()))
                },{
                    loading.postValue(false)
                    handleNetworkError(it)
                })
        )
    }

    fun doLaunchEditProfile(){

        launchEditProfile.postValue(Event(mapOf()))
    }
}