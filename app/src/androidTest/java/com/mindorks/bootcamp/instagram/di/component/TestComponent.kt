package com.mindorks.bootcamp.instagram.di.component

import com.mindorks.bootcamp.instagram.di.module.ApplicationTestModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationTestModule::class])
interface TestComponent : ApplicationComponent{
}