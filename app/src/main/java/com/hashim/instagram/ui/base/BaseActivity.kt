package com.hashim.instagram.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.hashim.instagram.InstagramApplication
import com.hashim.instagram.di.component.ActivityComponent
import com.hashim.instagram.di.component.DaggerActivityComponent
import com.hashim.instagram.di.module.ActivityModule
import com.hashim.instagram.ui.home.HomeFragment
import com.hashim.instagram.ui.photo.PhotoFragment
import com.hashim.instagram.ui.profile.ProfileFragment
import com.hashim.instagram.utils.display.Toaster
import javax.inject.Inject

/**
 * Reference for generics: https://kotlinlang.org/docs/reference/generics.html
 * Basically BaseActivity will take any class that extends BaseViewModel
 */
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    @Inject
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildActivityComponent())
        super.onCreate(savedInstanceState)
        setContentView(provideLayoutId())
        setupObservers()
        setupView(savedInstanceState)
        viewModel.onCreate()
    }

    private fun buildActivityComponent() =
        DaggerActivityComponent
            .builder()
            .applicationComponent((application as InstagramApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()

    protected open fun setupObservers() {
        viewModel.messageString.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })
    }

    open fun showMessage(message: String) = Toaster.show(this, message)

    open fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    //open fun goBack() = onBackPressed()

//    override fun onBackPressed() {
//
//        if (findNavController().backStack.size > 0) {
//            supportFragmentManager.popBackStackImmediate()
//        }
//        else super.onBackPressed()
//    }



    protected abstract fun provideLayoutId(): View

    protected abstract fun injectDependencies(activityComponent: ActivityComponent)

    protected abstract fun setupView(savedInstanceState: Bundle?)
}