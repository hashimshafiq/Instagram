package com.hashim.instagram.di.component

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.hashim.instagram.InstagramApplication
import com.hashim.instagram.data.local.db.DatabaseService
import com.hashim.instagram.data.local.db.dao.PostDao
import com.hashim.instagram.data.remote.NetworkService
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.di.ApplicationContext
import com.hashim.instagram.di.TempDirectory
import com.hashim.instagram.di.module.ApplicationModule
import com.hashim.instagram.utils.common.GridSpacingItemDecoration
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(app: InstagramApplication)

    fun getApplication(): Application

    @ApplicationContext
    fun getContext(): Context

    /**
     * These methods are written in ApplicationComponent because the instance of
     * NetworkService is singleton and is maintained in the ApplicationComponent's implementation by Dagger
     * For NetworkService singleton instance to be accessible to say DummyActivity's DummyViewModel
     * this ApplicationComponent must expose a method that returns NetworkService instance
     * This method will be called when NetworkService is injected in DummyViewModel.
     * Also, in ActivityComponent you can find dependencies = [ApplicationComponent::class] to link this relationship
     */
    fun getNetworkService(): NetworkService

    fun getDatabaseService(): DatabaseService

    fun getSharedPreferences(): SharedPreferences

    fun getNetworkHelper(): NetworkHelper

    /**---------------------------------------------------------------------------
     * Dagger will internally create UserRepository instance using constructor injection.
     * Dependency through constructor
     * UserRepository ->
     *  [NetworkService -> Nothing is required],
     *  [DatabaseService -> Nothing is required],
     *  [UserPreferences -> [SharedPreferences -> provided by the function provideSharedPreferences in ApplicationModule class]]
     * So, Dagger will be able to create an instance of UserRepository by its own using constructor injection
     *---------------------------------------------------------------------------------
     */
    fun getUserRepository(): UserRepository

    fun getSchedulerProvider(): SchedulerProvider

    fun getCompositeDisposable(): CompositeDisposable

    fun getPostDao(): PostDao

    @TempDirectory
    fun getTempDirectory(): File

    fun getViewPreloadSizeProvider() : ViewPreloadSizeProvider<String>

    fun getGridItemDecoration() : GridSpacingItemDecoration

}