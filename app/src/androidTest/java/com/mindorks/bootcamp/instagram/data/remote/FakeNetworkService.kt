package com.mindorks.bootcamp.instagram.data.remote

import com.mindorks.bootcamp.instagram.data.model.Post
import com.mindorks.bootcamp.instagram.data.remote.request.*
import com.mindorks.bootcamp.instagram.data.remote.response.*
import io.reactivex.Single
import okhttp3.MultipartBody
import java.util.*

class FakeNetworkService : NetworkService {
    override fun doLoginCall(request: LoginRequest, apiKey: String): Single<LoginResponse> {
        TODO("Not yet implemented")
    }

    override fun doSignupCall(request: SignupRequest, apiKey: String): Single<SignupResponse> {
        TODO("Not yet implemented")
    }

    override fun doHomePostListCall(
        firstPostId: String?,
        lastPostId: String?,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<PostListResponse> {
        val creator1 = Post.User("id1","name1","profile1")
        val creator2 = Post.User("id2","name2","profile2")

        val likedBy = mutableListOf<Post.User>(
            Post.User("id3","name3","profile3"),
            Post.User("id4","name4","profile4")
        )

        val post1 = Post("id1","img1",400,400,creator1,likedBy, Date())
        val post2 = Post("id2","img2",400,400,creator2,likedBy,Date())

        val postListResponse = PostListResponse("statusCode","message", listOf(post1,post2))
        return Single.just(postListResponse)
    }

    override fun doPostLikeCall(
        request: PostLikeModifyRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun doPostUnlikeCall(
        request: PostLikeModifyRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun doProfileGetCall(
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<ProfileResponse> {
        TODO("Not yet implemented")
    }

    override fun doLogoutCall(
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun doProfileUpdateCall(
        profileUpdateRequest: ProfileUpdateRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun doImageUpload(
        image: MultipartBody.Part,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<ImageResponse> {
        TODO("Not yet implemented")
    }

    override fun doCreatePost(
        request: PostCreationRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<PostCreationResponse> {
        TODO("Not yet implemented")
    }

    override fun doMyPostsCall(
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<PostListResponse> {
        TODO("Not yet implemented")
    }

}