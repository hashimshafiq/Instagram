package com.hashim.instagram.di.module

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hashim.instagram.data.repository.PhotoRepository
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.di.TempDirectory
import com.hashim.instagram.ui.base.BaseActivity
import com.hashim.instagram.ui.home.post.likeduser.LikedUserAdapter
import com.hashim.instagram.ui.home.post.likeduser.LikedUserViewModel
import com.hashim.instagram.ui.login.LoginViewModel
import com.hashim.instagram.ui.main.MainSharedViewModel
import com.hashim.instagram.ui.main.MainViewModel
import com.hashim.instagram.ui.profile.editprofile.EditProfileViewModel
import com.hashim.instagram.ui.signup.SignupViewModel
import com.hashim.instagram.ui.splash.SplashViewModel
import com.hashim.instagram.utils.ViewModelProviderFactory
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import java.io.File

/**
 * Kotlin Generics Reference: https://kotlinlang.org/docs/reference/generics.html
 * Basically it means that we can pass any class that extends BaseActivity which take
 * BaseViewModel subclass as parameter
 */
@Module
class ActivityModule(private val activity: BaseActivity<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(activity)

    @Provides
    fun providesLikedUserAdapter() = LikedUserAdapter(activity.lifecycle, ArrayList())

    @Provides
    fun provideSplashViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): SplashViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(SplashViewModel::class) {
            SplashViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
            //this lambda creates and return SplashViewModel
        }).get(SplashViewModel::class.java)

    @Provides
    fun providesLikedUserViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): LikedUserViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(LikedUserViewModel::class) {
            LikedUserViewModel(schedulerProvider, compositeDisposable, networkHelper)
            //this lambda creates and return SplashViewModel
        }).get(LikedUserViewModel::class.java)


    @Provides
    fun provideLoginViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): LoginViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(LoginViewModel::class) {
            LoginViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
        }).get(LoginViewModel::class.java)

    @Provides
    fun provideSignupViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): SignupViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(SignupViewModel::class) {
            SignupViewModel(schedulerProvider, compositeDisposable, networkHelper,userRepository)
        }).get(SignupViewModel::class.java)



    @Provides
    fun provideMainViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): MainViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(MainViewModel::class) {
            MainViewModel(schedulerProvider, compositeDisposable, networkHelper,userRepository)
        }).get(MainViewModel::class.java)



    @Provides
    fun provideMainSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): MainSharedViewModel = ViewModelProvider(
        activity, ViewModelProviderFactory(MainSharedViewModel::class) {
            MainSharedViewModel(schedulerProvider, compositeDisposable, networkHelper)
        }).get(MainSharedViewModel::class.java)
}