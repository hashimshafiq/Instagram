package com.hashim.instagram.ui.main

import androidx.lifecycle.MutableLiveData
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers

class MainViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(coroutineDispatchers, networkHelper) {

    private val profileNavigation = MutableLiveData<Event<Boolean>>()
    private val homeNavigation = MutableLiveData<Event<Boolean>>()

    override fun onCreate() {
        if (!userRepository.isThemeChange()) {
            homeNavigation.postValue(Event(true))

        }else{

            profileNavigation.postValue(Event(true))
        }

        userRepository.saveThemeChange(false)
        //homeNavigation.postValue(Event(true))
    }


}