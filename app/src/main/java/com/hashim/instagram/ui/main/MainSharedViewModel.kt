package com.hashim.instagram.ui.main

import androidx.lifecycle.MutableLiveData
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainSharedViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    override fun onCreate() {}

    val homeRedirection = MutableLiveData<Event<Boolean>>()

    val profileRedirection = MutableLiveData<Event<Boolean>>()

    val newPost: MutableLiveData<Event<Post>> = MutableLiveData()

    fun onHomeRedirect() {
        homeRedirection.postValue(Event(true))
    }

    fun onProfileRedirect(){
        profileRedirection.postValue(Event(true))
    }
}