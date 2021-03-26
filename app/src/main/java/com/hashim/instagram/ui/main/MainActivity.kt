package com.hashim.instagram.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hashim.instagram.R
import com.hashim.instagram.databinding.ActivityMainBinding
import com.hashim.instagram.di.component.ActivityComponent
import com.hashim.instagram.ui.base.BaseActivity
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity<MainViewModel>() {

    lateinit var binding: ActivityMainBinding

    companion object {
        const val TAG = "MainActivity"
    }

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel
    private var activeFragment: Fragment? = null
    private lateinit var fragmentStack : ArrayDeque<Int>

    override fun provideLayoutId(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun injectDependencies(activityComponent: ActivityComponent) = activityComponent.inject(
        this
    )

    override fun setupView(savedInstanceState: Bundle?) {
        fragmentStack = ArrayDeque<Int>()

        val navController = findNavController(R.id.containerFragment)

        binding.bottomNavigation.setupWithNavController(navController)

    }


    override fun setupObservers() {
        super.setupObservers()


        mainSharedViewModel.profileRedirection.observe(this, {
            it.getIfNotHandled()?.run {
                binding.bottomNavigation.selectedItemId = R.id.itemProfile
            }
        })

        mainSharedViewModel.homeRedirection.observe(this, {
            it.getIfNotHandled()?.run {
                binding.bottomNavigation.selectedItemId = R.id.itemHome
            }
        })



    }

}
