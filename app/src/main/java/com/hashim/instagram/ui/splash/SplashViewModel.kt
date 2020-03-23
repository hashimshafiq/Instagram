package com.hashim.instagram.ui.splash

import androidx.lifecycle.MutableLiveData
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class SplashViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    // Event is used by the view model to tell the activity to launch another Activity
    // view model also provided the Bundle in the event that is needed for the Activity
    val launchMain: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    override fun onCreate() {
        if(userRepository.getCurrentUser()!=null){
            launchMain.postValue(Event(emptyMap()))
        }else{
            launchLogin.postValue(Event(emptyMap()))
        }

    }
}