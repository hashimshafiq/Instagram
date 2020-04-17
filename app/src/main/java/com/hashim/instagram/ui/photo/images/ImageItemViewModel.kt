package com.hashim.instagram.ui.photo.images

import androidx.lifecycle.LiveData
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseItemViewModel
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ImageItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository
) : BaseItemViewModel<Image>(schedulerProvider,compositeDisposable,networkHelper){

    val imageDetail : LiveData<Image> = data

    override fun onCreate() {}


}