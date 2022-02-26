package com.hashim.instagram.ui.home

import androidx.lifecycle.MutableLiveData
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.repository.PostRepository
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.common.Resource
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor

class HomeViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val allPostList : ArrayList<Post>,
    private val paginator : PublishProcessor<Pair<String?,String?>>

) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val loading : MutableLiveData<Boolean> = MutableLiveData()
    val posts : MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val refreshPosts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val isLoggedIn: MutableLiveData<Event<Boolean>> = MutableLiveData()

    var firstId: String? = null
    var lastId: String? = null

    private val user : User? = userRepository.getCurrentUser()

    init {


        val isLogin = user != null



        if (isLogin){
            loading.postValue(true)
            compositeDisposable.add(
                paginator
                    .onBackpressureDrop()
                    .doOnNext{
                        loading.postValue(true)
                    }
                    .concatMapSingle {pageIds ->
                        return@concatMapSingle postRepository
                            .fetchHomePostList(pageIds.first,pageIds.second,user!!)
                            .subscribeOn(schedulerProvider.io())
                            .doOnError{
                                handleNetworkError(it)
                            }
                    }
                    .subscribe({
                        allPostList.addAll(it)
                        firstId = allPostList.maxByOrNull { post -> post.createdAt.time }?.id
                        lastId = allPostList.minByOrNull { post -> post.createdAt.time }?.id
                        loading.postValue(false)
                        posts.postValue(Resource.success(it))
                    },{
                        loading.postValue(false)
                        handleNetworkError(it)
                    })
            )
        }else{
            isLoggedIn.postValue(Event(isLogin))
        }

    }



    override fun onCreate() {
        loadMorePosts()
    }

    private fun loadMorePosts(){
         paginator.onNext(Pair(firstId,lastId))
    }

    fun onLoadMore(){
        if(loading.value !== null && loading.value == false) loadMorePosts()
    }

    fun onNewPost(post: Post) {
        allPostList.add(0, post)
        refreshPosts.postValue(Resource.success(mutableListOf<Post>().apply { addAll(allPostList) }))
    }

    private fun onDeletePost(post: Post){
        allPostList.remove(post)
        refreshPosts.postValue(Resource.success(mutableListOf<Post>().apply { addAll(allPostList) }))
    }

    fun deleteUserPost(post: Post) {
        if(networkHelper.isNetworkConnected()){
            loading.postValue(true)
            compositeDisposable.addAll(
                postRepository.deleteUserPost(post.id, user!!)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        loading.postValue(false)
                        onDeletePost(post)

                    }, {
                        loading.postValue(false)
                        handleNetworkError(it)
                    }
                    )
            )
        }
    }









}