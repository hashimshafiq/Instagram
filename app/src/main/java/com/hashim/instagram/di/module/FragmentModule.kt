package com.hashim.instagram.di.module

import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hashim.instagram.data.repository.PhotoRepository
import com.hashim.instagram.data.repository.PostRepository
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.di.TempDirectory
import com.hashim.instagram.ui.base.BaseFragment
import com.hashim.instagram.ui.home.HomeViewModel
import com.hashim.instagram.ui.home.post.PostsAdapter
import com.hashim.instagram.ui.home.post.likeduser.LikedUserAdapter
import com.hashim.instagram.ui.home.post.likeduser.LikedUserViewModel
import com.hashim.instagram.ui.login.LoginViewModel
import com.hashim.instagram.ui.main.MainSharedViewModel
import com.hashim.instagram.ui.photo.PhotoViewModel
import com.hashim.instagram.ui.photo.gallery.GalleryAdapter
import com.hashim.instagram.ui.profile.ProfileViewModel
import com.hashim.instagram.ui.profile.editprofile.EditProfileViewModel
import com.hashim.instagram.ui.profile.userposts.UserPostAdapter
import com.hashim.instagram.ui.signup.SignupViewModel
import com.hashim.instagram.ui.splash.SplashViewModel
import com.hashim.instagram.utils.ViewModelProviderFactory
import com.hashim.instagram.utils.common.Constants
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import com.mindorks.paracamera.Camera
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import java.io.File

@Module
class FragmentModule(private val fragment: BaseFragment<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun providePostsAdapter() = PostsAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideImagesAdapter() = GalleryAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun providesLikedUserAdapter() = LikedUserAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideArrayAdapter() = ArrayAdapter(fragment.requireContext(),android.R.layout.simple_spinner_item, ArrayList<String>())

    @Provides
    fun provideGridLayoutManager(): GridLayoutManager = GridLayoutManager(fragment.context,Constants.SPAN_COUNT)


    @Provides
    fun provideUserPostsAdapter() = UserPostAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideHomeViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        postRepository: PostRepository
    ): HomeViewModel = ViewModelProvider(
        fragment, ViewModelProviderFactory(HomeViewModel::class) {
            HomeViewModel(schedulerProvider, compositeDisposable, networkHelper,userRepository,postRepository,
                ArrayList(),
                PublishProcessor.create()
            )
        }).get(HomeViewModel::class.java)

    @Provides
    fun providePhotoViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        photoRepository: PhotoRepository,
        postRepository: PostRepository,
        @TempDirectory directory: File
    ): PhotoViewModel = ViewModelProvider(
        fragment, ViewModelProviderFactory(PhotoViewModel::class) {
            PhotoViewModel(schedulerProvider, compositeDisposable, networkHelper,userRepository, photoRepository, postRepository, directory)
        }).get(PhotoViewModel::class.java)

    @Provides
    fun provideProfileViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        postRepository: PostRepository
    ): ProfileViewModel = ViewModelProvider(
        fragment, ViewModelProviderFactory(ProfileViewModel::class) {
            ProfileViewModel(schedulerProvider, compositeDisposable, networkHelper,userRepository,postRepository)
        }).get(ProfileViewModel::class.java)

    @Provides
    fun provideCamera() = Camera.Builder()
        .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
        .setTakePhotoRequestCode(1)
        .setDirectory("temp")
        .setName("camera_temp_img")
        .setImageFormat(Camera.IMAGE_JPEG)
        .setCompression(75)
        .setImageHeight(500)// it will try to achieve this height as close as possible maintaining the aspect ratio;
        .build(fragment)

    @Provides
    fun provideMainSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): MainSharedViewModel = ViewModelProvider(
        fragment.activity!!, ViewModelProviderFactory(MainSharedViewModel::class) {
            MainSharedViewModel(schedulerProvider, compositeDisposable, networkHelper)
        }).get(MainSharedViewModel::class.java)

    @Provides
    fun provideEditProfileViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        photoRepository: PhotoRepository,
        @TempDirectory directory: File
    ): EditProfileViewModel = ViewModelProvider(
        fragment, ViewModelProviderFactory(EditProfileViewModel::class) {
            EditProfileViewModel(schedulerProvider, compositeDisposable, networkHelper,userRepository,photoRepository,directory)
        }).get(EditProfileViewModel::class.java)


    @Provides
    fun providesLikedUserViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): LikedUserViewModel = ViewModelProvider(
        fragment, ViewModelProviderFactory(LikedUserViewModel::class) {
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
        fragment, ViewModelProviderFactory(LoginViewModel::class) {
            LoginViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
        }).get(LoginViewModel::class.java)


    @Provides
    fun provideSignupViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): SignupViewModel = ViewModelProvider(
        fragment, ViewModelProviderFactory(SignupViewModel::class) {
            SignupViewModel(schedulerProvider, compositeDisposable, networkHelper,userRepository)
        }).get(SignupViewModel::class.java)


    @Provides
    fun provideSplashViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): SplashViewModel = ViewModelProvider(
        fragment, ViewModelProviderFactory(SplashViewModel::class) {
            SplashViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
            //this lambda creates and return SplashViewModel
        }).get(SplashViewModel::class.java)
}