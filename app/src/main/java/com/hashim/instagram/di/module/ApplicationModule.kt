package com.hashim.instagram.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.hashim.instagram.BuildConfig
import com.hashim.instagram.InstagramApplication
import com.hashim.instagram.data.local.db.DatabaseService
import com.hashim.instagram.data.local.db.dao.PostDao
import com.hashim.instagram.data.remote.NetworkService
import com.hashim.instagram.data.remote.Networking
import com.hashim.instagram.di.ApplicationContext
import com.hashim.instagram.di.TempDirectory
import com.hashim.instagram.utils.common.Constants
import com.hashim.instagram.utils.common.FileUtils
import com.hashim.instagram.utils.common.GridSpacingItemDecoration
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers
import com.hashim.instagram.utils.rx.CoroutineDispatchersProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: InstagramApplication) {


    @Singleton
    @Provides
    fun provideApplication(): Application = application


    @Singleton
    @ApplicationContext
    @Provides
    fun provideContext(): Context = application

    @Provides
    @Singleton
    @TempDirectory
    fun provideTempDirectory() = FileUtils.getDirectory(application, "temp")

    @Provides
    fun provideSchedulerProvider(): CoroutineDispatchers = CoroutineDispatchersProvider()

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        application.getSharedPreferences("bootcamp-instagram-project-prefs", Context.MODE_PRIVATE)

    /**
     * We need to write @Singleton on the provide method if we are create the instance inside this method
     * to make it singleton. Even if we have written @Singleton on the instance's class
     */
    @Provides
    @Singleton
    fun provideDatabaseService(): DatabaseService =
        Room.databaseBuilder(
            application, DatabaseService::class.java,
            "bootcamp-instagram-project-db"
        ).build()

    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService =
        Networking.create(
            BuildConfig.API_KEY,
            BuildConfig.BASE_URL,
            application.cacheDir,
            10 * 1024 * 1024 // 10MB
        )

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper = NetworkHelper(application)

    @Provides
    @Singleton
    fun providePostDao(
        database: DatabaseService): PostDao = database.postDao()

    @Provides
    @Singleton
    fun provideViewPreloadSizeProvider(): ViewPreloadSizeProvider<String> = ViewPreloadSizeProvider<String>()

    @Provides
    fun providesGridItemDecoration(): GridSpacingItemDecoration = GridSpacingItemDecoration(
        Constants.SPAN_COUNT,Constants.SPAN_SPACING,Constants.IS_EDGE_CASE)
}