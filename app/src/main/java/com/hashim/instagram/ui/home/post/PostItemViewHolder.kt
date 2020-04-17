package com.hashim.instagram.ui.home.post

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.di.component.ViewHolderComponent
import com.hashim.instagram.ui.base.BaseItemViewHolder
import com.hashim.instagram.ui.home.onClickListener
import com.hashim.instagram.ui.home.post.likeduser.LikedUserActivity
import com.hashim.instagram.utils.common.GlideHelper
import com.hashim.instagram.utils.display.Toaster
import kotlinx.android.synthetic.main.item_view_post.view.*

class PostItemViewHolder(parent: ViewGroup,private val onClickListener: onClickListener) : BaseItemViewHolder<Post,PostItemViewModel>(R.layout.item_view_post,parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.name.observe(this, Observer {
            itemView.tvName.text = it
        })

        viewModel.postTime.observe(this, Observer {
            itemView.tvTime.text = it
        })

        viewModel.likesCount.observe(this, Observer {
            itemView.tvLikesCount.text = itemView.context.getString(R.string.post_like_label, it)
        })

        viewModel.isLiked.observe(this, Observer {
            if (it) itemView.ivLike.setImageResource(R.drawable.ic_heart_selected)
            else itemView.ivLike.setImageResource(R.drawable.ic_heart_unselected)
        })


        viewModel.profileImage.observe(this, Observer {
            it?.run {
                val glideRequest = Glide
                    .with(itemView.ivProfile.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = itemView.ivProfile.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    itemView.ivProfile.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_unselected))
                }
                glideRequest.into(itemView.ivProfile)
            }
        })

        viewModel.imageDetail.observe(this, Observer {
            it?.run {
                val glideRequest = Glide
                    .with(itemView.ivPost.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))

                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = itemView.ivPost.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    itemView.ivPost.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_photo))
                }
                glideRequest.into(itemView.ivPost)
            }
        })

        viewModel.launchLikesDetail.observe(this, Observer {
            it.getIfNotHandled()?.run {
                this["data"]?.run {
                    val intent = Intent(itemView.context,LikedUserActivity::class.java)
                    intent.putParcelableArrayListExtra("data",ArrayList(this))
                    itemView.context.startActivity(intent)
                }
            }
        })

        viewModel.sharePost.observe(this, Observer {
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
                ContextCompat.startActivity(itemView.context,Intent.createChooser(shareIntent, "Share Image"),null)
            }
        })
    }

    override fun setupView(view: View) {
        itemView.ivLike.setOnClickListener { viewModel.onLikeClick() }
        itemView.ivProfile.setOnClickListener { viewModel.onProfilePhotoClicked(onClickListener) }
        itemView.tvName.setOnClickListener { viewModel.onProfilePhotoClicked(onClickListener) }
        itemView.ivPost.setOnLongClickListener { viewModel.userPostClick(onClickListener) }
        itemView.tvLikesCount.setOnClickListener { viewModel.onLikeCountClick() }
        itemView.tvShare.setOnClickListener { viewModel.onShareClick(itemView.ivPost.drawable.toBitmap(itemView.ivPost.width,itemView.ivPost.height,Bitmap.Config.ARGB_8888)) }
    }
}