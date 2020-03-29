package com.hashim.instagram.data.repository

import com.hashim.instagram.data.local.db.dao.PostDao
import com.hashim.instagram.data.local.db.entity.PostEntity
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.NetworkService
import com.hashim.instagram.data.remote.request.PostCreationRequest
import com.hashim.instagram.data.remote.request.PostLikeModifyRequest
import com.hashim.instagram.data.remote.response.GeneralResponse
import com.hashim.instagram.data.remote.response.PostListResponse
import com.hashim.instagram.utils.common.Resource
import com.hashim.instagram.utils.network.NetworkBoundResource
import com.hashim.instagram.utils.network.NetworkHelper
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val networkService: NetworkService,
    private val networkHelper: NetworkHelper,
    private val postDao: PostDao
) {



    fun fetchHomePostList(firstPostId: String?, lastPostId: String?, user: User): Single<List<Post>> {
        return networkService.doHomePostListCall(
            firstPostId,
            lastPostId,
            user.id,
            user.accessToken
        ).map { it.data }
    }

    fun makeLikePost(post: Post, user: User): Single<Post> {
        return networkService.doPostLikeCall(
            PostLikeModifyRequest(post.id),
            user.id,
            user.accessToken
        ).map {
            post.likedBy?.apply {
                this.find { postUser -> postUser.id == user.id } ?: this.add(
                    Post.User(
                        user.id,
                        user.name,
                        user.profilePicUrl
                    )
                )
            }
            return@map post
        }
    }

    fun makeUnlikePost(post: Post, user: User): Single<Post> {
        return networkService.doPostUnlikeCall(
            PostLikeModifyRequest(post.id),
            user.id,
            user.accessToken
        ).map {
            post.likedBy?.apply {
                this.find { postUser -> postUser.id == user.id }?.let { this.remove(it) }
            }
            return@map post
        }
    }

    fun createPost(imgUrl : String, imgWidth : Int, imgHeight : Int, user: User) : Single<Post>{
        return networkService.doCreatePost(PostCreationRequest(imgUrl,imgWidth, imgHeight),user.id,user.accessToken).map {
            Post(
                it.data.id,
                it.data.imageUrl,
                it.data.imageWidth,
                it.data.imageHeight,
                Post.User(
                    user.id,
                    user.name,
                    user.profilePicUrl
                ),
                mutableListOf(),
                it.data.createdAt
            )
        }

    }


    fun fetchUserPostList(user: User) : Single<List<Post>> =
        networkService.doMyPostsCall(user.id,user.accessToken).map { it.data }

    fun deleteUserPost(postId: String, user: User) : Single<GeneralResponse> =
        networkService.doPostDelete(postId,user.id,user.accessToken)



}