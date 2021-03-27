package com.hashim.instagram.ui.profile

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.Networking
import com.hashim.instagram.data.repository.PostRepository
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class ProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    override fun onCreate() {
        fetchProfileData()
        fetchUserPostList()
    }


    val name : MutableLiveData<String> = MutableLiveData()
    val profile : MutableLiveData<Image> = MutableLiveData()
    val tagLine : MutableLiveData<String> = MutableLiveData()
    val loading : MutableLiveData<Boolean> = MutableLiveData()
    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val userPosts : MutableLiveData<List<Post>> = MutableLiveData()
    val numberOfPosts : MutableLiveData<Int> = MutableLiveData()



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

    private fun fetchUserPostList(){
        compositeDisposable.add(
            postRepository.fetchUserPostList(user)
                .subscribeOn(schedulerProvider.io())
                .subscribe (
                    {
                        userPosts.postValue(it)
                        numberOfPosts.postValue(it.size)
                    },
                    {
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


}