package com.hashim.instagram.ui.home.post

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.Networking
import com.hashim.instagram.data.repository.PostRepository
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseItemViewModel
import com.hashim.instagram.ui.home.onClickListener
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.common.Resource
import com.hashim.instagram.utils.common.TimeUtils
import com.hashim.instagram.utils.display.ScreenUtils
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class PostItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseItemViewModel<Post>(schedulerProvider, compositeDisposable, networkHelper) {

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

    fun onLikeClick() = data.value?.let {
        if(networkHelper.isNetworkConnected()){
            val api = if(isLiked.value == true) postRepository.makeUnlikePost(it,user) else postRepository.makeLikePost(it,user)
            compositeDisposable.add(api
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    responsePost -> if (responsePost.id == it.id) updateData(it)
                },{error->
                    handleNetworkError(error)
                }
                )
            )

        }else{
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
        }
    }

    fun onProfilePhotoClicked(onClickListener: onClickListener){
        data.value?.let {
            if(it.creator.id==user.id)
                onClickListener.onClickPhoto()
        }
    }

    fun userPostClick(onClickListener: onClickListener) : Boolean {
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