package com.hashim.instagram.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.Networking
import com.hashim.instagram.data.repository.PostRepository
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseViewModel(coroutineDispatchers, networkHelper) {


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

    override fun onCreate() {

            fetchProfileData()
            fetchUserPostList()


    }

    private fun fetchProfileData(){

        viewModelScope.launch(coroutineDispatchers.io()) {
            loading.postValue(true)
            val deferred = async { userRepository.doUserProfileFetch(user) }
            try {
                val response = deferred.await()

                response.collect {
                    loading.postValue(false)
                    name.postValue(it.data?.name)
                    profile.postValue(it.data?.profilePicUrl?.run {
                        Image(this,headers)
                    })
                    tagLine.postValue(it.data?.tagLine)
                }

            }catch (ex: Exception){
                loading.postValue(false)
                handleNetworkError(ex)
            }

        }

    }

    private fun fetchUserPostList(){

        viewModelScope.launch(coroutineDispatchers.io()) {

            val deferred = async { postRepository.fetchUserPostList(user) }
            try {
                val response = deferred.await()
                response.collect {
                    userPosts.postValue(it)
                    numberOfPosts.postValue(it.size)
                }

            }catch (ex: Exception){
                handleNetworkError(ex)
            }

        }

    }

    fun doLogout(){

        viewModelScope.launch(coroutineDispatchers.io()) {
            loading.postValue(true)
            val deferred = async { userRepository.doLogout(user) }
            try {
                val response = deferred.await()
                response.collect {
                    loading.postValue(false)
                    userRepository.removeCurrentUser()
                    launchLogin.postValue(Event(mapOf()))
                }

            }catch (ex: Exception){
                loading.postValue(false)
                handleNetworkError(ex)
            }
        }

    }


}