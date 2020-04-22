package com.hashim.instagram.data.repository

import com.hashim.instagram.data.local.db.dao.PostDao
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.NetworkService
import com.hashim.instagram.data.remote.Networking
import com.hashim.instagram.data.remote.response.PostListResponse
import com.hashim.instagram.utils.network.NetworkBoundResource
import com.hashim.instagram.utils.network.NetworkHelper
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

//@RunWith(MockitoJUnitRunner::class)
//class PostRepositoryTest {
//
//    @Mock
//    lateinit var networkService: NetworkService
//
//    @Mock
//    lateinit var networkBoundResource: NetworkBoundResource
//
//    @Mock
//    lateinit var postDao: PostDao
//
//    @Mock
//    lateinit var networkHelper: NetworkHelper
//
//    lateinit var postRepository: PostRepository
//
//
//    @Before
//    fun setUp(){
//        Networking.API_KEY = "FAKE API"
//        postRepository = PostRepository(networkService,networkHelper,postDao)
//    }

//    @Test
//    fun fetchHomePostList_requestDoHomePostList(){
//
//        val user = User("id","name","email","accessToken","profilePicUrl")
//
////        doReturn(Single.just(PostListResponse("statusCode","message", listOf())))
////            .`when`(networkService)
////            .doHomePostListCall(
////                "firstId",
////                "lastId",
////                user.id,
////                user.accessToken,
////                Networking.API_KEY
////            )
//
//        doReturn(Single.just((PostListResponse("statusCode","message", listOf()))))
//            .`when`(networkBoundResource)
//            .asSingle()
//
//            postRepository.fetchHomePostList("firstId","lastId",user)
//
////            verify(networkService).doHomePostListCall(
////                "firstId",
////                "lastId",
////                user.id,
////                user.accessToken,
////                Networking.API_KEY
////            )
//
//        verify(networkBoundResource).asSingle()
//
//    }



//}