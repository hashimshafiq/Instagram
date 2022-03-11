package com.hashim.instagram.ui.photo.gallery

import androidx.lifecycle.LiveData
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseItemViewModel
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers
import javax.inject.Inject

class GalleryItemViewModel @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper,
    userRepository: UserRepository
) : BaseItemViewModel<Image>(coroutineDispatchers,networkHelper){

    val imageDetail : LiveData<Image> = data

    override fun onCreate() {}


}