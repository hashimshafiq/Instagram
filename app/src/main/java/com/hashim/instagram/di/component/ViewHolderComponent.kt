package com.hashim.instagram.di.component

import com.hashim.instagram.di.ViewModelScope
import com.hashim.instagram.di.module.ViewHolderModule
import com.hashim.instagram.ui.home.post.PostItemViewHolder
import com.hashim.instagram.ui.home.post.likeduser.LikedUserItemViewHolder
import com.hashim.instagram.ui.photo.gallery.GalleryItemViewHolder
import com.hashim.instagram.ui.profile.userposts.UserPostItemViewHolder
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {



    fun inject(viewHolder: PostItemViewHolder)
    fun inject(viewHolder : UserPostItemViewHolder)
    fun inject(viewHolder : LikedUserItemViewHolder)
    fun inject(viewHolder : GalleryItemViewHolder)
}