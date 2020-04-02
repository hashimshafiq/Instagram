package com.hashim.instagram.utils.network

import com.hashim.instagram.data.model.Post
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


abstract class NetworkBoundResource() {

    private val result: Single<List<Post>>

    init {
        // Lazy disk observable.
        val diskObservable = Single.defer {
            loadFromDb()
                // Read from disk on Computation Scheduler
                .subscribeOn(Schedulers.computation())
        }

        // Lazy network observable.
        val networkObservable = Single.defer {
            createCall()
                // Request API on IO Scheduler
                .subscribeOn(Schedulers.io())
                // Read/Write to disk on Computation Scheduler
                .observeOn(Schedulers.computation())
                .doOnSuccess { request ->

                        saveCallResult(request)

                }
                .doOnError {
                    it
                }
                //.flatMap { loadFromDb() }
        }

        result = if (shouldFetch()) {
                networkObservable
                .map { it }
                .doOnError { it }
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())

        }
            else { diskObservable
                .map { it }
                .doOnError { it }
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())


        }
    }

    fun asSingle(): Single<List<Post>> {
        return result
    }

    protected abstract fun saveCallResult(request: List<Post>)

    protected abstract fun loadFromDb(): Single<List<Post>>

    protected abstract fun createCall(): Single<List<Post>>

    protected abstract fun shouldFetch(): Boolean
}