package com.hashim.instagram.utils.network

import com.hashim.instagram.data.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow


abstract class NetworkBoundResource() {

    fun asFlow(): Flow<List<Post>> {
        return flow {

            //fetch database contents firsts
            val db = loadFromDb().first()

            //fetch from network
            val network = createCall()

            if (shouldFetch()){
                saveCallResult(network.first())
                emit(network.first())
            }else{
                emit(db)
            }
        }
    }

    protected abstract suspend fun saveCallResult(request: List<Post>)

    protected abstract suspend fun loadFromDb(): Flow<List<Post>>

    protected abstract suspend fun createCall(): Flow<List<Post>>

    protected abstract fun shouldFetch(): Boolean


}