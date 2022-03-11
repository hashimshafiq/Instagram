package com.hashim.instagram.ui.photo.gallery

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.databinding.ItemViewGridPostBinding
import com.hashim.instagram.di.component.ViewHolderComponent
import com.hashim.instagram.ui.base.BaseItemViewHolder


class GalleryItemViewHolder(parent : ViewGroup):BaseItemViewHolder<Image,GalleryItemViewModel>(R.layout.item_view_grid_post,parent) {

    lateinit var binding: ItemViewGridPostBinding

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupView(view: View) {

        binding = ItemViewGridPostBinding.bind(view)

        binding.ivPost.setOnClickListener {
            GalleryAdapter.RxBus.itemClickStream.tryEmit(viewModel.data.value!!.url)
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.imageDetail.observe(this) {
            it?.run {
                val glideRequest = Glide
                    .with(binding.ivPost.context)
                    .load(url)

                glideRequest.into(binding.ivPost)

            }
        }
    }




}