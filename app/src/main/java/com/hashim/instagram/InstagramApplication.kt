package com.hashim.instagram

import android.app.Application
import com.hashim.instagram.di.component.ApplicationComponent
import com.hashim.instagram.di.component.DaggerApplicationComponent
import com.hashim.instagram.di.module.ApplicationModule

class InstagramApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
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
}