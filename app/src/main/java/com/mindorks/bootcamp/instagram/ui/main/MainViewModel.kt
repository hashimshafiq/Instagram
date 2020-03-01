package com.mindorks.bootcamp.instagram.ui.main

import android.util.EventLog
import androidx.lifecycle.MutableLiveData
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val profileNavigation = MutableLiveData<Event<Boolean>>()
    val homeNavigation = MutableLiveData<Event<Boolean>>()
    val addPhotoNavigation = MutableLiveData<Event<Boolean>>()


    override fun onCreate() {
        homeNavigation.postValue(Event(true))
    }

    fun onProfileSelected(){
        profileNavigation.postValue(Event(true))
    }

    fun onAddPhotoSelected(){
        addPhotoNavigation.postValue(Event(true))
    }

    fun onHomeSelected(){
        homeNavigation.postValue(Event(true))
    }
}