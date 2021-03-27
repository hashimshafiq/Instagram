package com.hashim.instagram.di.component

import com.hashim.instagram.di.ActivityScope
import com.hashim.instagram.di.module.ActivityModule
import com.hashim.instagram.ui.main.MainActivity
import com.hashim.instagram.ui.splash.SplashFragment
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {


    fun inject(activity: MainActivity)
}