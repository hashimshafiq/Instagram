package com.hashim.instagram.di.module

import androidx.lifecycle.ViewModelProvider
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseDialog
import com.hashim.instagram.ui.profile.settings.SettingsDialogViewModel
import com.hashim.instagram.utils.ViewModelProviderFactory
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers
import dagger.Module
import dagger.Provides

@Module
class DialogFragmentModule(private val fragment: BaseDialog<*>) {

    @Provides
    fun provideSettingsDialogViewModel(
        coroutineDispatchers: CoroutineDispatchers,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): SettingsDialogViewModel = ViewModelProvider(
        fragment, ViewModelProviderFactory(SettingsDialogViewModel::class) {
            SettingsDialogViewModel(coroutineDispatchers, networkHelper,userRepository)
        }).get(SettingsDialogViewModel::class.java)



}