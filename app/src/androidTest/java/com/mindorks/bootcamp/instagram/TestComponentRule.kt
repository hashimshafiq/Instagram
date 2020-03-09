package com.mindorks.bootcamp.instagram

import android.content.Context
import com.mindorks.bootcamp.instagram.di.component.DaggerTestComponent
import com.mindorks.bootcamp.instagram.di.component.TestComponent
import com.mindorks.bootcamp.instagram.di.module.ApplicationTestModule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestComponentRule(private val context: Context) : TestRule {

    var testComponent : TestComponent? = null

    fun getContext() = context

    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement(){
            @Throws(Throwable::class)
            override fun evaluate() {
                try {
                    setDaggerTestComponentInApplication()
                    base.evaluate()
                }finally {
                    testComponent = null
                }
            }

        }
    }

    private fun setDaggerTestComponentInApplication(){
        val application = context.applicationContext as InstagramApplication
        testComponent = DaggerTestComponent.builder()
            .applicationTestModule(ApplicationTestModule(application))
            .build()
        application.setComponent(testComponent!!)
    }
}