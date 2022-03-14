package com.hashim.instagram.utils.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.hashim.instagram.data.model.Post
import kotlinx.coroutines.flow.*


abstract class NetworkBoundResource() {

    fun asFlow(): Flow<List<Post>> {
        return flow {

            //fetch database contents firsts
            val db = loadFromDb()
            emit(db.first())


            //fetch from network
            val network = createCall()
            saveCallResult(network)

            emitAll(db)

        }.catch {

        }
    }

    @WorkerThread
    protected abstract suspend fun saveCallResult(request: List<Post>)

    @MainThread
    protected abstract fun loadFromDb(): Flow<List<Post>>

    @MainThread
    protected abstract suspend fun createCall(): List<Post>

}