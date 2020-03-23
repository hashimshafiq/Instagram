package com.hashim.instagram.ui.profile.userposts

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.di.component.ViewHolderComponent
import com.hashim.instagram.ui.base.BaseItemViewHolder
import com.hashim.instagram.utils.common.GlideHelper
import kotlinx.android.synthetic.main.item_view_grid_post.view.*

class UserPostItemViewHolder(parent : ViewGroup):BaseItemViewHolder<Post,UserPostItemViewModel>(R.layout.item_view_grid_post,parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupView(view: View) {

    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.imageDetail.observe(this, Observer {
            it?.run {
                val glideRequest = Glide
                    .with(itemView.ivPost.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))

                glideRequest.into(itemView.ivPost)
            }
        })


    }
}