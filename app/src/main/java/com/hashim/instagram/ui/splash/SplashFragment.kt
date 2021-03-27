package com.hashim.instagram.ui.splash

import android.view.View
import androidx.navigation.fragment.findNavController
import com.hashim.instagram.R
import com.hashim.instagram.databinding.FragmentSplashBinding
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment

class SplashFragment : BaseFragment<SplashViewModel>() {

    private var _binding: FragmentSplashBinding? = null

    private val binding get() = _binding!!

    companion object {
        const val TAG = "SplashActivity"
    }

    override fun provideLayoutId(): Int = R.layout.fragment_splash

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {

        _binding = FragmentSplashBinding.bind(view)
    }

    override fun setupObservers() {
        // Event is used by the view model to tell the activity to launch another activity
        // view model also provided the Bundle in the event that is needed for the Activity
        viewModel.launchMain.observe(this, {
            it.getIfNotHandled()?.run {
                findNavController().navigate(R.id.action_splashFragment_to_itemHome)
            }
        })

        viewModel.launchLogin.observe(this, {
            it.getIfNotHandled()?.run {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}