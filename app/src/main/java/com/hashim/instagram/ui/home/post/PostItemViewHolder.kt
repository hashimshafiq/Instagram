package com.hashim.instagram.ui.home.post

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.databinding.ItemViewPostBinding
import com.hashim.instagram.di.component.ViewHolderComponent
import com.hashim.instagram.ui.base.BaseItemViewHolder
import com.hashim.instagram.ui.home.HomeFragmentDirections
import com.hashim.instagram.ui.home.OnClickListener
import com.hashim.instagram.utils.common.GlideHelper

class PostItemViewHolder(parent: ViewGroup,private val onClickListener: OnClickListener) : BaseItemViewHolder<Post,PostItemViewModel>(R.layout.item_view_post,parent) {

    lateinit var binding: ItemViewPostBinding

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupView(view: View) {

        binding = ItemViewPostBinding.bind(view)

        with(binding){
            ivLike.setOnClickListener { viewModel.onLikeClick() }
            ivProfile.setOnClickListener { viewModel.onProfilePhotoClicked(onClickListener) }
            tvName.setOnClickListener { viewModel.onProfilePhotoClicked(onClickListener) }
            ivPost.setOnLongClickListener { viewModel.userPostClick(onClickListener) }
            tvLikesCount.setOnClickListener {
                viewModel.onLikeCountClick()
            }
            tvShare.setOnClickListener { viewModel.onShareClick(ivPost.drawable.toBitmap(ivPost.width,ivPost.height,Bitmap.Config.ARGB_8888)) }
            ivPost.setOnClickListener { viewModel.onImageClick() }
        }

    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.name.observe(this) {
            binding.tvName.text = it
        }

        viewModel.postTime.observe(this) {
            binding.tvTime.text = it
        }

        viewModel.likesCount.observe(this) {
            binding.tvLikesCount.text = itemView.context.getString(R.string.post_like_label, it)
        }

        viewModel.isLiked.observe(this) {
            if (it) binding.ivLike.setImageResource(R.drawable.ic_heart_selected)
            else binding.ivLike.setImageResource(R.drawable.ic_heart_unselected)
        }


        viewModel.profileImage.observe(this) {
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
                    binding.ivProfile.layoutParams = params
                    glideRequest.apply {
                        apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        apply(RequestOptions.placeholderOf(R.drawable.ic_profile_unselected))
                    }
                }
                glideRequest.into(binding.ivProfile)
            }
        }

        viewModel.imageDetail.observe(this) {
            it?.run {
                val glideRequest = Glide
                    .with(binding.ivPost.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))

                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = binding.ivPost.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    binding.ivPost.layoutParams = params
                    glideRequest.apply {
                        apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        apply(RequestOptions.placeholderOf(R.drawable.ic_photo))
                    }
                }
                glideRequest.into(binding.ivPost)
            }
        }


        viewModel.launchLikesDetail.observe(this) {
            it.getIfNotHandled()?.run {
                this["data"]?.run {
                    val direction =
                        HomeFragmentDirections.actionItemHomeToLikedUserFragment(this.toTypedArray())
                    itemView.findNavController().navigate(direction)
                }
            }
        }

        viewModel.sharePost.observe(this) {
            it.getIfNotHandled()?.run {
                val path: String = MediaStore.Images.Media.insertImage(
                    itemView.context.contentResolver,
                    this,
                    null,
                    null
                )
                val uri: Uri = Uri.parse(path)
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                shareIntent.type = "image/*"
                ContextCompat.startActivity(
                    itemView.context,
                    Intent.createChooser(shareIntent, "Share Image"),
                    null
                )
            }
        }

        viewModel.launchDetailFragment.observe(this){
            it.getIfNotHandled()?.let { image ->
                onClickListener.onClickPhoto(binding.ivPost, image)
            }
        }
    }


}