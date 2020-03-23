package com.hashim.instagram.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.hashim.instagram.R
import com.hashim.instagram.di.component.ActivityComponent
import com.hashim.instagram.ui.base.BaseActivity
import com.hashim.instagram.ui.login.LoginActivity
import com.hashim.instagram.ui.main.MainActivity
import com.hashim.instagram.utils.common.Event

class SplashActivity : BaseActivity<SplashViewModel>() {

    companion object {
        const val TAG = "SplashActivity"
    }

    override fun provideLayoutId(): Int = R.layout.activity_splash

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
    }

    override fun setupObservers() {
        // Event is used by the view model to tell the activity to launch another activity
        // view model also provided the Bundle in the event that is needed for the Activity
        viewModel.launchMain.observe(this, Observer<Event<Map<String, String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        })

        viewModel.launchLogin.observe(this, Observer<Event<Map<String,String>>> {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext,LoginActivity::class.java))
            }
        })
    }
}