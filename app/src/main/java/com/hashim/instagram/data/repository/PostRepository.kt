package com.hashim.instagram.data.repository

import com.hashim.instagram.data.local.db.dao.PostDao
import com.hashim.instagram.data.local.db.entity.PostWithUser
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.NetworkService
import com.hashim.instagram.data.remote.request.PostCreationRequest
import com.hashim.instagram.data.remote.request.PostLikeModifyRequest
import com.hashim.instagram.data.remote.response.GeneralResponse
import com.hashim.instagram.utils.common.Resource
import com.hashim.instagram.utils.log.Logger
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.network.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val networkService: NetworkService,
    private val networkHelper: NetworkHelper,
    private val postDao: PostDao
) {

    companion object {
        const val TAG = "PostRepository"
    }

    fun fetchHomePostList(firstPostId: String?, lastPostId: String?, user: User): Flow<Resource<List<Post>>> {

        return networkBoundResource(
            query = { postDao.getAll().map { processData(it) } },
            fetch = { networkService.doHomePostListCall(firstPostId, lastPostId, user.id, user.accessToken)},
            saveFetchResult = { apiResponse ->
                if (apiResponse.statusCode == "success" && apiResponse.data.isNotEmpty()) {
                    postDao.preparePostAndCreator(apiResponse.data)
                }
            },
            shouldFetch = { networkHelper.isNetworkConnected() },
            coroutineDispatcher = Dispatchers.IO
        )

    }

    fun makeLikePost(post: Post, user: User): Flow<Post> {

        return flow {
            networkService.doPostLikeCall(
                PostLikeModifyRequest(post.id),
                user.id,
                user.accessToken
            )
            post.likedBy?.apply {
                this.find { postUser -> postUser.id == user.id } ?: this.add(
                    Post.User(
                        user.id,
                        user.name,
                        user.profilePicUrl
                    )
                )
            }

            emit(post)
        }.flowOn(Dispatchers.IO)
    }

    fun makeUnlikePost(post: Post, user: User): Flow<Post> {

        return flow {
            networkService.doPostUnlikeCall(
                PostLikeModifyRequest(post.id),
                user.id,
                user.accessToken
            )
            post.likedBy?.apply {
                this.find { postUser -> postUser.id == user.id }?.let { this.remove(it) }
            }
            emit(post)
        }.flowOn(Dispatchers.IO)
    }

    fun createPost(imgUrl : String, imgWidth : Int, imgHeight : Int, user: User) : Flow<Post>{

        return flow {
            val response = networkService.doCreatePost(PostCreationRequest(imgUrl,imgWidth, imgHeight),user.id,user.accessToken)

            emit(Post(
                response.data.id,
                response.data.imageUrl,
                response.data.imageWidth,
                response.data.imageHeight,
                Post.User(
                    user.id,
                    user.name,
                    user.profilePicUrl
                ),
                mutableListOf(),
                response.data.createdAt
            ))
        }.flowOn(Dispatchers.IO)
    }


    fun fetchUserPostList(user: User) : Flow<List<Post>> {

        return flow {
            val api = networkService.doMyPostsCall(user.id,user.accessToken)
            emit(api.data)
        }.flowOn(Dispatchers.IO)
    }


    fun deleteUserPost(postId: String, user: User) : Flow<GeneralResponse> {
        return flow {
            val api = networkService.doPostDelete(postId,user.id,user.accessToken)
            emit(api)
        }.flowOn(Dispatchers.IO)
    }



    private fun processData(listItems : List<PostWithUser>) : List<Post>{
        val arrayList = mutableListOf<Post>()
        for (item in listItems) {
            for (items in item.postEntity) {
                arrayList.add(
                    Post(
                        items.id,
                        items.imageUrl,
                        items.imageWidth,
                        items.imageHeight,
                        Post.User(
                            item.userEntity.id,
                            item.userEntity.name,
                            item.userEntity.profilePicUrl
                        ),
                        mutableListOf(),
                        items.createdAt
                    )
                )
                Logger.d(TAG, items.toString())
            }
        }
        return arrayList.toList()
    }

}