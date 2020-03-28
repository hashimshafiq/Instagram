package com.hashim.instagram.ui.home.post.likeduser

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.di.component.ActivityComponent
import com.hashim.instagram.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_liked_user.*
import javax.inject.Inject

class LikedUserActivity : BaseActivity<LikedUserViewModel>() {

    override fun provideLayoutId(): Int = R.layout.activity_liked_user

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    @Inject
    lateinit var likedUserAdapter: LikedUserAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun setupView(savedInstanceState: Bundle?) {

        if(intent.hasExtra("data")){
            intent.getParcelableArrayListExtra<Post.User>("data").run {
                viewModel.loadData(this)
            }
        }

        rvLike.apply {
            layoutManager = linearLayoutManager
            adapter = likedUserAdapter

        }
    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.likedUsers.observe(this, Observer {
            likedUserAdapter.appendData(it.data!!)
            tvLikesNumber.text = getString(R.string.user_post_likes_label,it.data.size)
        })


    }
}
