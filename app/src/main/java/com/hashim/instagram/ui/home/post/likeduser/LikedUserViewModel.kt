package com.hashim.instagram.ui.home.post.likeduser

import androidx.lifecycle.MutableLiveData
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.Resource
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class LikedUserViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper){

    val likedUsers : MutableLiveData<Resource<List<Post.User>>> = MutableLiveData()


    override fun onCreate() {

    }

    fun loadData(users : List<Post.User>) {
        likedUsers.postValue(Resource.success(users))
    }
}