package com.hashim.instagram.ui.main

import androidx.lifecycle.MutableLiveData
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.common.Resource
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers

class MainSharedViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper
) : BaseViewModel(coroutineDispatchers, networkHelper) {

    override fun onCreate() {}

    val homeRedirection = MutableLiveData<Event<Boolean>>()

    val profileRedirection = MutableLiveData<Event<Boolean>>()

    val newPost: MutableLiveData<Event<Post>> = MutableLiveData()

    val snackBar : MutableLiveData<Event<Resource<Int>>> = MutableLiveData()



    fun onHomeRedirect() {
        homeRedirection.postValue(Event(true))
    }

    fun onProfileRedirect(){
        profileRedirection.postValue(Event(true))
    }

    fun showSnackBar(message : Resource<Int>){
        snackBar.postValue(Event(message))
    }


}