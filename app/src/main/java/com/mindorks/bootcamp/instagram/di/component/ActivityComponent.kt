package com.mindorks.bootcamp.instagram.di.component

import com.mindorks.bootcamp.instagram.di.ActivityScope
import com.mindorks.bootcamp.instagram.di.module.ActivityModule
import com.mindorks.bootcamp.instagram.ui.login.LoginActivity
import com.mindorks.bootcamp.instagram.ui.main.MainActivity
import com.mindorks.bootcamp.instagram.ui.profile.editprofile.EditProfileActivity
import com.mindorks.bootcamp.instagram.ui.signup.SignupActivity
import com.mindorks.bootcamp.instagram.ui.splash.SplashActivity
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