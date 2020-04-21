package com.hashim.instagram.di.module

import androidx.lifecycle.ViewModelProvider
import com.hashim.instagram.data.repository.PostRepository
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseDialog
import com.hashim.instagram.ui.main.MainSharedViewModel
import com.hashim.instagram.ui.profile.ProfileViewModel
import com.hashim.instagram.ui.profile.settings.SettingsDialogViewModel
import com.hashim.instagram.utils.ViewModelProviderFactory
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class DialogFragmentModule(private val fragment: BaseDialog<*>) {

    @Provides
    fun provideSettingsDialogViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): SettingsDialogViewModel = ViewModelProvider(
        fragment, ViewModelProviderFactory(SettingsDialogViewModel::class) {
            SettingsDialogViewModel(schedulerProvider, compositeDisposable, networkHelper,userRepository)
        }).get(SettingsDialogViewModel::class.java)



}