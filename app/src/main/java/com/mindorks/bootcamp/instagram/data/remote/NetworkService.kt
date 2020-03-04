package com.mindorks.bootcamp.instagram.data.remote

import com.mindorks.bootcamp.instagram.data.remote.request.DummyRequest
import com.mindorks.bootcamp.instagram.data.remote.request.LoginRequest
import com.mindorks.bootcamp.instagram.data.remote.request.SignupRequest
import com.mindorks.bootcamp.instagram.data.remote.response.DummyResponse
import com.mindorks.bootcamp.instagram.data.remote.response.LoginResponse
import com.mindorks.bootcamp.instagram.data.remote.response.PostListResponse
import com.mindorks.bootcamp.instagram.data.remote.response.SignupResponse
import io.reactivex.Single
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface NetworkService {


    @POST(Endpoints.DUMMY)
    fun doDummyCall(
        @Body request: DummyRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
    ): Single<DummyResponse>

    @POST(Endpoints.LOGIN)
    fun doLoginCall(
        @Body request: LoginRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
    ): Single<LoginResponse>

    @POST(Endpoints.SIGNUP)
    fun doSignupCall(
        @Body request: SignupRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
    ): Single<SignupResponse>

    @GET(Endpoints.ALL_POST_LIST)
    fun doHomePostListCall(
        @Query("firstPostId") firstPostId: String?,
        @Query("lastPostId") lastPostId: String?,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<PostListResponse>






}