package com.hashim.instagram.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hashim.instagram.R
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment
import com.hashim.instagram.ui.login.LoginActivity
import com.hashim.instagram.ui.profile.editprofile.EditProfileActivity
import com.hashim.instagram.ui.profile.settings.SettingsDialog
import com.hashim.instagram.ui.profile.userposts.UserPostAdapter
import com.hashim.instagram.utils.common.GlideHelper
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject


class ProfileFragment : BaseFragment<ProfileViewModel>() {

    companion object {

        const val TAG = "ProfileFragment"

        fun newInstance(): ProfileFragment {
            val args = Bundle()
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var userPostAdapter: UserPostAdapter

    @Inject
    lateinit var gridLayoutManager : GridLayoutManager

    override fun provideLayoutId(): Int = R.layout.fragment_profile

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        tvLogout.setOnClickListener {
            viewModel.doLogout()
        }

        bt_edit_profile.setOnClickListener{
            viewModel.doLaunchEditProfile()
        }

        tvNighMode.setOnClickListener {
           // viewModel.doSetTheme(tvNighMode.text.toString())
            viewModel.doLaunchSettingDialog()
        }

        rvPosts.apply {
            layoutManager = gridLayoutManager
            adapter = userPostAdapter
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.name.observe(this, Observer {
            tvName.text = it
        })

        viewModel.loading.observe(this, Observer {
            pb_loading.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.tagLine.observe(this, Observer {
            tvDescription.text = it
        })

        viewModel.profile.observe(this, Observer {
            it?.run {
                val glideRequest = Glide
                    .with(ivProfile.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                glideRequest.into(ivProfile)
            }
        })

        viewModel.launchLogin.observe(this, Observer {
            it.getIfNotHandled()?.run {
                val intent = Intent(context,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)


            }
        })

        viewModel.launchEditProfile.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(Intent(context, EditProfileActivity::class.java))
            }
        })

        viewModel.numberOfPosts.observe(this, Observer {
            tvPostNumber.text = getString(R.string.post_number_label,it)
        })

        viewModel.userPosts.observe(this, Observer {
            userPostAdapter.appendData(it)
        })

        viewModel.isLightMode.observe(this, Observer {
            if (!it){
                tvNighMode.text = "Light Mode"
            }else{
                tvNighMode.text = getString(R.string.night_mode)
            }

        })

        viewModel.launchSettingsDialog.observe(this, Observer {
            it.getIfNotHandled()?.run {
                SettingsDialog.newInstance().show(requireActivity().supportFragmentManager,"SettingsDialog")
            }
        })
    }
}
