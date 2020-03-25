package com.hashim.instagram.ui.main

import android.os.Bundle
import androidx.annotation.IntegerRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.hashim.instagram.R
import com.hashim.instagram.di.component.ActivityComponent
import com.hashim.instagram.ui.base.BaseActivity
import com.hashim.instagram.ui.home.HomeFragment
import com.hashim.instagram.ui.photo.PhotoFragment
import com.hashim.instagram.ui.profile.ProfileFragment
import com.hashim.instagram.utils.display.Toaster
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity<MainViewModel>(),FragmentManager.OnBackStackChangedListener {

    companion object {
        const val TAG = "MainActivity"
    }

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel
    private var activeFragment: Fragment? = null
    private lateinit var fragmentStack : ArrayDeque<Int>

    override fun provideLayoutId(): Int = R.layout.activity_main

    override fun injectDependencies(activityComponent: ActivityComponent) = activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        fragmentStack = ArrayDeque<Int>()
        //supportFragmentManager.addOnBackStackChangedListener(this)

        bottomNavigation.run {
            itemIconTintList = null
            setOnNavigationItemSelectedListener {
                when (it.itemId){
                    R.id.itemHome -> {
                        viewModel.onHomeSelected()
                        true
                    }
                    R.id.itemAddPhotos -> {
                        viewModel.onAddPhotoSelected()
                        true
                    }
                    R.id.itemProfile -> {
                        viewModel.onProfileSelected()
                        true
                    }
                    else -> false
                }
            }
        }

    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.homeNavigation.observe(this, Observer {
            it.getIfNotHandled()?.run {
                showHome()
            }
        })

        viewModel.addPhotoNavigation.observe(this, Observer {
            it.getIfNotHandled()?.run {
                showAddPhoto()
            }
        })

        viewModel.profileNavigation.observe(this, Observer {
            it.getIfNotHandled()?.run {
                showProfile()
            }
        })

        mainSharedViewModel.profileRedirection.observe(this, Observer {
            it.getIfNotHandled()?.run {
                bottomNavigation.selectedItemId = R.id.itemProfile
            }
        })

        mainSharedViewModel.homeRedirection.observe(this, Observer {
            it.getIfNotHandled()?.run {
                bottomNavigation.selectedItemId = R.id.itemHome
            }
        })

    }

    private fun showHome(){
        if (activeFragment is HomeFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG) as HomeFragment?
        if(fragment == null){
            fragment = HomeFragment.newInstance()
            fragmentTransaction.add(R.id.containerFragment,fragment,HomeFragment.TAG)
            //fragmentTransaction.addToBackStack(HomeFragment.TAG)
            fragmentStack.add(R.id.itemHome)
        }else{
            fragmentTransaction.show(fragment)
        }

        if(activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()
        activeFragment = fragment

    }

    private fun showAddPhoto(){
        if (activeFragment is PhotoFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(PhotoFragment.TAG) as PhotoFragment?
        if(fragment == null){
            fragment = PhotoFragment.newInstance()
            fragmentTransaction.add(R.id.containerFragment,fragment,PhotoFragment.TAG)
            //fragmentTransaction.addToBackStack(PhotoFragment.TAG)
            fragmentStack.add(R.id.itemAddPhotos)
        }else{
            fragmentTransaction.show(fragment)
        }
        if(activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)
        fragmentTransaction.commit()
        activeFragment = fragment

    }

    private fun showProfile(){
        if (activeFragment is ProfileFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) as ProfileFragment?
        if(fragment == null){
            fragment = ProfileFragment.newInstance()
            fragmentTransaction.add(R.id.containerFragment,fragment,ProfileFragment.TAG)
            //fragmentTransaction.addToBackStack(ProfileFragment.TAG)
            fragmentStack.add(R.id.itemProfile)
        }else{
            fragmentTransaction.show(fragment)
        }
        if(activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)
        fragmentTransaction.commit()
        activeFragment = fragment

    }

    override fun onBackStackChanged() {
        Toaster.show(applicationContext,"POP")


    }

    override fun onBackPressed() {
       if(fragmentStack.size>1){
           fragmentStack.removeLast()
           bottomNavigation.selectedItemId = fragmentStack.peekLast()

       }else{
            super.onBackPressed()
       }

    }
}
