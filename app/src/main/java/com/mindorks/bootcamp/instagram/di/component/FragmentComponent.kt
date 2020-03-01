package com.mindorks.bootcamp.instagram.di.component

import com.mindorks.bootcamp.instagram.di.FragmentScope
import com.mindorks.bootcamp.instagram.di.module.FragmentModule
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.ui.dummies.DummiesFragment
import com.mindorks.bootcamp.instagram.ui.home.HomeFragment
import com.mindorks.bootcamp.instagram.ui.home.HomeViewModel
import com.mindorks.bootcamp.instagram.ui.photo.PhotoFragment
import com.mindorks.bootcamp.instagram.ui.profile.ProfileFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {

    fun inject(fragment: DummiesFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: PhotoFragment)
    fun inject(fragment: ProfileFragment)



}