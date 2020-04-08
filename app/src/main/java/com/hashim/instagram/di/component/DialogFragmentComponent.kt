package com.hashim.instagram.di.component

import com.hashim.instagram.di.FragmentScope
import com.hashim.instagram.di.module.DialogFragmentModule
import com.hashim.instagram.di.module.FragmentModule
import com.hashim.instagram.ui.home.HomeFragment
import com.hashim.instagram.ui.photo.PhotoFragment
import com.hashim.instagram.ui.profile.ProfileFragment
import com.hashim.instagram.ui.profile.settings.SettingsDialog
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [DialogFragmentModule::class]
)
interface DialogFragmentComponent {

    fun inject(dialogFragment : SettingsDialog)

}