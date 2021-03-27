package com.hashim.instagram.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hashim.instagram.R
import com.hashim.instagram.databinding.ActivityMainBinding
import com.hashim.instagram.di.component.ActivityComponent
import com.hashim.instagram.ui.base.BaseActivity
import javax.inject.Inject


class MainActivity : BaseActivity<MainViewModel>() {

    lateinit var binding: ActivityMainBinding

    companion object {
        const val TAG = "MainActivity"
    }

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    lateinit var navController : NavController

    override fun provideLayoutId(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun injectDependencies(activityComponent: ActivityComponent) = activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {


        navController = findNavController(R.id.containerFragment)

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            onDestinationChanged(destination)
        }


    }

    private fun onDestinationChanged(destination: NavDestination) {

        when(destination.id){
                R.id.settingsDialog,
                R.id.itemHome,
                R.id.itemAddPhotos,
                R.id.itemProfile -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    binding.viewShadow.visibility = View.VISIBLE
                }

            else -> {
                binding.bottomNavigation.visibility = View.GONE
                binding.viewShadow.visibility = View.GONE
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
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

//    override fun onBackPressed() {
//        if(navController.currentDestination?.id != R.id.itemHome){
//            navController.popBackStack()
//        }
//        else super.onBackPressed()
//    }
}
