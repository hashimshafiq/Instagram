package com.hashim.instagram.di.component

import com.hashim.instagram.data.local.db.dao.PostDao
import com.hashim.instagram.di.module.ApplicationTestModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationTestModule::class])
interface TestComponent : ApplicationComponent{


}