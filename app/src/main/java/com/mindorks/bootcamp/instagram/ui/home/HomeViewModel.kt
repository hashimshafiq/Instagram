package com.mindorks.bootcamp.instagram.ui.home

import androidx.lifecycle.MutableLiveData
import com.mindorks.bootcamp.instagram.data.model.Post
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.PostRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor

class HomeViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val allpostList : ArrayList<Post>,
    private val paginator : PublishProcessor<Pair<String?,String?>>

) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val loading : MutableLiveData<Boolean> = MutableLiveData()
    val posts : MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val refreshPosts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()

    var firstId: String? = null
    var lastId: String? = null

    private val user : User = userRepository.getCurrentUser()!!

    init {
        loading.postValue(true)
        compositeDisposable.add(
            paginator
                .onBackpressureDrop()
                .doOnNext{
                    loading.postValue(true)
                }
                .concatMapSingle {pageIds ->
                    return@concatMapSingle postRepository
                        .fetchHomePostList(pageIds.first,pageIds.second,user)
                        .subscribeOn(schedulerProvider.io())
                        .doOnError{
                            handleNetworkError(it)
                        }
                }
                .subscribe({
                    allpostList.addAll(it)
                    firstId = allpostList.maxBy { post -> post.createdAt.time }?.id
                    lastId = allpostList.minBy { post -> post.createdAt.time }?.id
                    loading.postValue(false)
                    posts.postValue(Resource.success(it))
                },{
                    loading.postValue(false)
                    handleNetworkError(it)
                })
        )

    }



    override fun onCreate() {
        loadMorePosts()
    }

    private fun loadMorePosts(){
        if(checkInternetConnectionWithMessage()) paginator.onNext(Pair(firstId,lastId))
    }

    fun onLoadMore(){
        if(loading.value !== null && loading.value == false) loadMorePosts()
    }

    fun onNewPost(post: Post) {
        allpostList.add(0, post)
        refreshPosts.postValue(Resource.success(mutableListOf<Post>().apply { addAll(allpostList) }))
    }


}