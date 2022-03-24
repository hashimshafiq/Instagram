package com.hashim.instagram.ui.profile.userposts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.Networking
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseItemViewModel
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.display.ScreenUtils
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers
import javax.inject.Inject

class UserPostItemViewModel @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper,
    userRepository: UserRepository
) : BaseItemViewModel<Post>(coroutineDispatchers,networkHelper){


    private val user : User = userRepository.getCurrentUser()!!
    private val screenWidth = ScreenUtils.getScreenWidth()
    private val screenHeight = ScreenUtils.getScreenHeight()
    val launchDetailFragment : MutableLiveData<Event<Image>> = MutableLiveData()

    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )

    val imageDetail : LiveData<Image> = Transformations.map(data){
        Image(it.imageUrl, headers)
    }

    private fun calculateScaleFactor(post: Post) =
        post.imageWidth?.let { return@let screenWidth.toFloat() / it } ?: 1f



    override fun onCreate() {}
    fun onPostClicked() {
        data.value?.let {
            val image = Image(
                it.imageUrl,
                headers,
                screenWidth,
                it.imageHeight?.let { height ->
                    return@let (calculateScaleFactor(it) * height) .toInt()
                } ?: screenHeight/3
            )
            launchDetailFragment.postValue(Event(image))
        }
    }
}