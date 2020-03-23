package com.hashim.instagram.di.module

import androidx.lifecycle.LifecycleRegistry
import com.hashim.instagram.di.ViewModelScope
import com.hashim.instagram.ui.base.BaseItemViewHolder
import dagger.Module
import dagger.Provides

@Module
class ViewHolderModule(private val viewHolder: BaseItemViewHolder<*, *>) {

    @Provides
    @ViewModelScope
    fun provideLifecycleRegistry(): LifecycleRegistry = LifecycleRegistry(viewHolder)


}