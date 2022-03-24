package com.hashim.instagram.data.repository

import com.hashim.instagram.data.local.db.dao.PostDao
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.NetworkService
import com.hashim.instagram.data.remote.Networking
import com.hashim.instagram.data.remote.response.PostListResponse
import com.hashim.instagram.utils.network.NetworkHelper
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PostRepositoryTest {

    @Mock
    lateinit var networkService: NetworkService

    @Mock
    lateinit var postDao: PostDao

    @Mock
    lateinit var networkHelper: NetworkHelper

    lateinit var postRepository: PostRepository


    @Before
    fun setUp(){
        Networking.API_KEY = "FAKE API KEY"
        postRepository = PostRepository(networkService,networkHelper,postDao)
    }

    @Test
    fun fetchHomePostList_requestDoHomePostList(){

        val user = User("id","name","email","accessToken","profilePicUrl")

        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()

        postRepository.fetchHomePostList("firstId","lastId",user)

        verify(postDao).getAll()






    }



}