package com.mindorks.bootcamp.instagram.ui.dummy

import android.os.Bundle
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.dummies.DummiesFragment

class DummyActivity : BaseActivity<DummyViewModel>() {

    companion object {
        const val TAG = "DummyActivity"
    }

    override fun provideLayoutId(): Int = R.layout.activity_dummy

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        addDummiesFragment()
    }

    private fun addDummiesFragment() {
        supportFragmentManager.findFragmentByTag(DummiesFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .add(R.id.container_fragment, DummiesFragment.newInstance(), DummiesFragment.TAG)
            .commitAllowingStateLoss()
    }
}