package com.hashim.instagram.ui.profile.userposts

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.databinding.ItemViewGridPostBinding
import com.hashim.instagram.di.component.ViewHolderComponent
import com.hashim.instagram.ui.base.BaseItemViewHolder
import com.hashim.instagram.utils.common.GlideHelper

class UserPostItemViewHolder(parent : ViewGroup):BaseItemViewHolder<Post,UserPostItemViewModel>(R.layout.item_view_grid_post,parent) {

    lateinit var binding: ItemViewGridPostBinding

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupView(view: View) {
        binding = ItemViewGridPostBinding.bind(view)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.imageDetail.observe(this) {
            it?.run {
                val glideRequest = Glide
                    .with(binding.ivPost.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))

                glideRequest.into(binding.ivPost)
            }
        }


    }
}