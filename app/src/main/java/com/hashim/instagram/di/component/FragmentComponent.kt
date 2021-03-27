package com.hashim.instagram.di.component

import com.hashim.instagram.di.FragmentScope
import com.hashim.instagram.di.module.FragmentModule
import com.hashim.instagram.ui.home.HomeFragment
import com.hashim.instagram.ui.photo.PhotoFragment
import com.hashim.instagram.ui.profile.ProfileFragment
import com.hashim.instagram.ui.profile.editprofile.EditProfileFragment
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


}