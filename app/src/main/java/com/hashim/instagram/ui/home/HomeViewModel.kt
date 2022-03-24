package com.hashim.instagram.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.repository.PostRepository
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.common.Resource
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val allPostList: ArrayList<Post>

) : BaseViewModel(coroutineDispatchers, networkHelper) {


    private val user: User? = userRepository.getCurrentUser()

    private val _posts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val posts: LiveData<Resource<List<Post>>> = _posts

    private val _refreshPosts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val refreshPosts: LiveData<Resource<List<Post>>> = _refreshPosts

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _isLoggedIn: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isLoggedIn: LiveData<Event<Boolean>> = _isLoggedIn

    private val _paginator: MutableSharedFlow<Pair<String?, String?>> =
        MutableSharedFlow(replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST)


    private var firstId: String? = null
    private var lastId: String? = null

    init {
        if (user!= null){

            _paginator
                .onEach {
                    onFetchHomePostList()
                }
                .catch {
                    messageStringId.postValue(Resource.error(R.string.try_again))
                }.launchIn(viewModelScope)
            }

        else {
            _isLoggedIn.postValue(Event(false))
        }
    }

    private fun onFetchHomePostList(){
        viewModelScope.launch(coroutineDispatchers.io()) {

            try {
                postRepository.fetchHomePostList(firstId, lastId, user!!)
                    .onStart { _loading.postValue(true) }
                    .collect {
                        _loading.postValue(false)
                        it.data?.let { it1 -> allPostList.addAll(it1.toMutableList()) }
                        firstId = allPostList.maxByOrNull { post -> post.createdAt.time }?.id
                        lastId = allPostList.minByOrNull { post -> post.createdAt.time }?.id
                        _posts.postValue(it)
                    }

            } catch (ex: Exception) {
                _loading.postValue(false)
                handleNetworkError(ex)
            }
        }

    }

    override fun onCreate() {
        loadMorePosts()
    }

    private fun loadMorePosts() {
        _paginator.tryEmit(Pair(firstId,lastId))
    }

    fun onLoadMore() {
        if(loading.value == false) loadMorePosts()
    }

    fun onNewPost(post: Post) {
        allPostList.add(0, post)
        _refreshPosts.postValue(Resource.success(mutableListOf<Post>().apply { addAll(allPostList) }))
    }

    private fun onDeletePost(post: Post) {
        allPostList.remove(post)
        _refreshPosts.postValue(Resource.success(mutableListOf<Post>().apply { addAll(allPostList) }))
    }

    fun deleteUserPost(post: Post) {
        if (networkHelper.isNetworkConnected()) {
            viewModelScope.launch(coroutineDispatchers.io()) {
                try {
                    postRepository.deleteUserPost(postId = post.id, user!!)
                        .collect {
                            onDeletePost(post)
                        }
                } catch (ex: Exception) {
                    handleNetworkError(ex)
                }
            }
        }
    }
}