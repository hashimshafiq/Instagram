package com.hashim.instagram.ui.home.post

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.Networking
import com.hashim.instagram.data.repository.PostRepository
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseItemViewModel
import com.hashim.instagram.ui.detail.DetailFragment
import com.hashim.instagram.ui.home.OnClickListener
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.common.Resource
import com.hashim.instagram.utils.common.TimeUtils
import com.hashim.instagram.utils.display.ScreenUtils
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


class PostItemViewModel @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseItemViewModel<Post>(coroutineDispatchers, networkHelper) {

    private val user : User = userRepository.getCurrentUser()!!
    private val screenWidth = ScreenUtils.getScreenWidth()
    private val screenHeight = ScreenUtils.getScreenHeight()
    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )

    val sharePost : MutableLiveData<Event<Bitmap>> = MutableLiveData()
    val launchLikesDetail: MutableLiveData<Event<Map<String,MutableList<Post.User>?>>> = MutableLiveData()
    val name : LiveData<String> = Transformations.map(data){ it.creator.name}
    val postTime : LiveData<String> = Transformations.map(data){TimeUtils.getTimeAgo(it.createdAt)}
    val likesCount : LiveData<Int> = Transformations.map(data){it.likedBy?.size}
    val isLiked : LiveData<Boolean> = Transformations.map(data){
        it.likedBy?.find {postUser -> postUser.id == user.id } !== null
    }

    val launchDetailFragment : MutableLiveData<Event<Image>> = MutableLiveData()

    val profileImage : LiveData<Image> = Transformations.map(data){
        it.creator.profilePicUrl?.run { Image(this,headers) }
    }

    val imageDetail : LiveData<Image> = Transformations.map(data){
        Image(
            it.imageUrl,
            headers,
            screenWidth,
            it.imageHeight?.let { height ->
                return@let (calculateScaleFactor(it) * height) .toInt()
            } ?: screenHeight/3
        )
    }

    private fun calculateScaleFactor(post: Post) =
        post.imageWidth?.let { return@let screenWidth.toFloat() / it } ?: 1f


    override fun onCreate() {}

    fun onLikeClick() {

        viewModelScope.launch(coroutineDispatchers.io()) {
            data.value?.let { post ->
                if (networkHelper.isNetworkConnected()) {
                    val api = if (isLiked.value == true) postRepository.makeUnlikePost(post, user)
                    else postRepository.makeLikePost(post, user)
                    val deferredApi = async { api }
                    try {
                        val response = deferredApi.await()
                        response.collect { responsePost ->
                            if (post.id == responsePost.id) updateData(responsePost)
                        }
                    }catch (ex : Exception){
                        handleNetworkError(ex)
                    }

                } else {
                    messageStringId.postValue(Resource.error(R.string.network_connection_error))
                }
            }


        }

    }

    fun onImageClick(){
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

    fun onProfilePhotoClicked(onClickListener: OnClickListener){
        data.value?.let {
            if(it.creator.id==user.id)
                onClickListener.onClickProfilePhoto()
        }
    }

    fun userPostClick(onClickListener: OnClickListener) : Boolean {
        data.value?.let {
            if(it.creator.id==user.id)
                onClickListener.onLongClickPost(it)
        }

        return true
    }

    fun onLikeCountClick() {
            launchLikesDetail.postValue(Event(mapOf("data" to data.value?.likedBy)))
    }

    fun onShareClick(bitmap: Bitmap){
        sharePost.value = Event(bitmap)
    }


}