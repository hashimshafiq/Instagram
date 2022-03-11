package com.hashim.instagram.data.remote

import com.hashim.instagram.data.model.Post
import com.hashim.instagram.data.remote.request.*
import com.hashim.instagram.data.remote.response.*
import okhttp3.MultipartBody
import java.util.*

class FakeNetworkService : NetworkService {
    override suspend fun doLoginCall(request: LoginRequest, apiKey: String): LoginResponse {
        return LoginResponse("200", 200, "login successful",
            "xxxxxxxxx","userId","username", "test@test.com","profileUrl")
    }

    override suspend fun doSignupCall(request: SignupRequest, apiKey: String): SignupResponse {
        return SignupResponse("200", 200, "login successful",
            "xxxxxxxxx","userId","username", "test@test.com","profileUrl")
    }

    override suspend fun doHomePostListCall(
        firstPostId: String?,
        lastPostId: String?,
        userId: String,
        accessToken: String,
        apiKey: String
    ): PostListResponse {
        val creator1 = Post.User("id1", "name1", "profile1")
        val creator2 = Post.User("id2", "name2", "profile2")

        val likedBy = mutableListOf<Post.User>(
            Post.User("id3", "name3", "profile3"),
            Post.User("id4", "name4", "profile4")
        )

        val post1 = Post("id1", "img1", 400, 400, creator1, likedBy, Date())
        val post2 = Post("id2", "img2", 400, 400, creator2, likedBy, Date())

        return PostListResponse("statusCode", "message", listOf(post1, post2))
    }

    override suspend fun doPostLikeCall(
        request: PostLikeModifyRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): GeneralResponse {
        return GeneralResponse("200", "Post Liked")
    }

    override suspend fun doPostUnlikeCall(
        request: PostLikeModifyRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): GeneralResponse {
        return GeneralResponse("200", "Post Unliked")
    }

    override suspend fun doProfileGetCall(
        userId: String,
        accessToken: String,
        apiKey: String
    ): ProfileResponse {
        val profileUser = ProfileResponse.User("id","name", "profile","tagline")
        return ProfileResponse("200",200,"Profile Response", profileUser)
    }

    override suspend fun doLogoutCall(
        userId: String,
        accessToken: String,
        apiKey: String
    ): GeneralResponse {
        return GeneralResponse("200","Logout successful")
    }

    override suspend fun doProfileUpdateCall(
        profileUpdateRequest: ProfileUpdateRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): GeneralResponse {
        return GeneralResponse("200", "Update successful")
    }

    override suspend fun doImageUpload(
        image: MultipartBody.Part,
        userId: String,
        accessToken: String,
        apiKey: String
    ): ImageResponse {
        return ImageResponse("200", 200, "Image upload successful",
                ImageResponse.ImageData("imageUrl")
            )
    }

    override suspend fun doCreatePost(
        request: PostCreationRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): PostCreationResponse {
        return PostCreationResponse("200", 200, "post created",
                PostCreationResponse.PostData("id","imageUrl",100,100,Date())
            )
    }

    override suspend fun doMyPostsCall(
        userId: String,
        accessToken: String,
        apiKey: String
    ): PostListResponse {
        return PostListResponse("200","successful", emptyList())
    }

    override suspend fun doPostDelete(
        postId: String,
        userId: String,
        accessToken: String,
        apiKey: String
    ): GeneralResponse {
        return GeneralResponse("200","post deleted")
    }

}