package com.hashim.instagram.ui.home.post.likeduser

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.databinding.ItemViewLikedUserBinding
import com.hashim.instagram.di.component.ViewHolderComponent
import com.hashim.instagram.ui.base.BaseItemViewHolder
import com.hashim.instagram.utils.common.GlideHelper

class LikedUserItemViewHolder(parent: ViewGroup) : BaseItemViewHolder<Post.User,LikedUserItemViewModel>(R.layout.item_view_liked_user,parent) {


    lateinit var binding: ItemViewLikedUserBinding



    override fun setupView(view: View) {

        binding = ItemViewLikedUserBinding.bind(view)
    }


    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.name.observe(this, {
            binding.tvName.text = it
        })



        viewModel.profileImage.observe(this, {
            it?.run {
                val glideRequest = Glide
                    .with(binding.ivProfile.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = binding.ivProfile.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    binding.root.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_unselected))
                }
                glideRequest.into(binding.ivProfile)
            }
        })

    }




}