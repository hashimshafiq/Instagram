package com.hashim.instagram.ui.profile.userposts

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.Networking
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseItemViewModel
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UserPostItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository
) : BaseItemViewModel<Post>(schedulerProvider,compositeDisposable,networkHelper){


    private val user : User = userRepository.getCurrentUser()!!

    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )

    val imageDetail : LiveData<Image> = Transformations.map(data){
        Image(it.imageUrl, headers)
    }





    override fun onCreate() {}
}