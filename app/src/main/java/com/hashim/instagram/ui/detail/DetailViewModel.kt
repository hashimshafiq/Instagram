package com.hashim.instagram.ui.detail

import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers

class DetailViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper,
) : BaseViewModel(coroutineDispatchers, networkHelper){


    override fun onCreate() {

    }
}