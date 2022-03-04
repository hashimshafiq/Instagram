package com.hashim.instagram

import android.app.Application
import android.content.SharedPreferences
import com.hashim.instagram.di.component.ApplicationComponent
import com.hashim.instagram.di.component.DaggerApplicationComponent
import com.hashim.instagram.di.module.ApplicationModule
import com.hashim.instagram.utils.ThemeManager
import javax.inject.Inject

class InstagramApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    @Inject
    lateinit var preferences : SharedPreferences

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
        initTheme()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }

    // Needed to replace the component with a test specific one
    fun setComponent(applicationComponent: ApplicationComponent) {
        this.applicationComponent = applicationComponent
    }

    private fun initTheme() {
        preferences.getString("PREF_THEME_MODE","Default")?.let { ThemeManager.applyTheme(it) }
    }
}