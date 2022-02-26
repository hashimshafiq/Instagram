package com.hashim.instagram.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.hashim.instagram.InstagramApplication
import com.hashim.instagram.di.component.DaggerFragmentComponent
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.di.module.FragmentModule
import com.hashim.instagram.utils.display.Toaster
import javax.inject.Inject

/**
 * Reference for generics: https://kotlinlang.org/docs/reference/generics.html
 * Basically BaseFragment will take any class that extends BaseViewModel
 */
abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    @Inject
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildFragmentComponent())
        super.onCreate(savedInstanceState)
        setupObservers()
        viewModel.onCreate()
    }

    private fun buildFragmentComponent() =
        DaggerFragmentComponent
            .builder()
            .applicationComponent((requireContext().applicationContext as InstagramApplication).applicationComponent)
            .fragmentModule(FragmentModule(this))
            .build()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        layoutInflater.inflate(provideLayoutId(), container, false)


    protected open fun setupObservers() {
        viewModel.messageString.observe(this) {
            it.data?.run { showMessage(this) }
        }

        viewModel.messageStringId.observe(this) {
            it.data?.run { showMessage(this) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }


    open fun showMessage(message: String) = context?.let { Toaster.show(it, message) }

    open fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

//    fun goBack() {
//        if (activity is BaseActivity<*>) (activity as BaseActivity<*>).goBack()
//    }



    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    protected abstract fun injectDependencies(fragmentComponent: FragmentComponent)

    protected abstract fun setupView(view: View)

}