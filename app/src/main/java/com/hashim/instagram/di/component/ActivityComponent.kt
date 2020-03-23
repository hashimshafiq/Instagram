package com.hashim.instagram.di.component

import com.hashim.instagram.di.ActivityScope
import com.hashim.instagram.di.module.ActivityModule
import com.hashim.instagram.ui.login.LoginActivity
import com.hashim.instagram.ui.main.MainActivity
import com.hashim.instagram.ui.profile.editprofile.EditProfileActivity
import com.hashim.instagram.ui.signup.SignupActivity
import com.hashim.instagram.ui.splash.SplashActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun inject(activity: SplashActivity)

    fun inject(activity: LoginActivity)

    fun inject(activity: SignupActivity)

    fun inject(activity: MainActivity)

    fun inject(activity: EditProfileActivity)
}