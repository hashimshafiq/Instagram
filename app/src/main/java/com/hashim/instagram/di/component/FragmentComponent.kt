package com.hashim.instagram.di.component

import com.hashim.instagram.di.FragmentScope
import com.hashim.instagram.di.module.FragmentModule
import com.hashim.instagram.ui.detail.DetailFragment
import com.hashim.instagram.ui.home.HomeFragment
import com.hashim.instagram.ui.home.post.likeduser.LikedUserFragment
import com.hashim.instagram.ui.login.LoginFragment
import com.hashim.instagram.ui.photo.PhotoFragment
import com.hashim.instagram.ui.profile.ProfileFragment
import com.hashim.instagram.ui.profile.editprofile.EditProfileFragment
import com.hashim.instagram.ui.signup.SignupFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {


    fun inject(fragment: HomeFragment)
    fun inject(fragment: PhotoFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: EditProfileFragment)
    fun inject(fragment: LikedUserFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: SignupFragment)
    fun inject(fragment: DetailFragment)


}