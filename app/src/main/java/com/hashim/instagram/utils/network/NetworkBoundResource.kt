package com.hashim.instagram.utils.network
//
//import androidx.annotation.MainThread
//import com.hashim.instagram.utils.common.Resource
//import com.hashim.instagram.utils.common.Status
//import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
//import io.reactivex.Flowable
//import io.reactivex.Single
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//
//
//abstract class NetworkBoundResource<ResultType, RequestType>() {
//
//    private val result: Single<Resource<ResultType>>
//
//    init {
//        // Lazy disk observable.
//        val diskObservable = Single.defer {
//            loadFromDb()
//                // Read from disk on Computation Scheduler
//                .subscribeOn(Schedulers.computation())
//        }
//
//        // Lazy network observable.
//        val networkObservable = Single.defer {
//            createCall()
//                // Request API on IO Scheduler
//                .subscribeOn(Schedulers.io())
//                // Read/Write to disk on Computation Scheduler
//                .observeOn(Schedulers.computation())
//                .doOnNext { request: Resource<RequestType> ->
//                    if (request.status==Status.SUCCESS) {
//                        saveCallResult(processResponse(request))
//                    }
//                }
//                .onErrorReturn { throwable: Throwable ->
//                    when (throwable) {
//                        is HttpException -> {
//                            throw Exceptions.propagate(NetworkExceptions.getNoServerConnectivityError(context))
//                        }
//                        is IOException -> {
//                            throw Exceptions.propagate(NetworkExceptions.getNoNetworkConnectivityError(context))
//                        }
//                        else -> {
//                            throw Exceptions.propagate(NetworkExceptions.getUnexpectedError(context))
//                        }
//                    }
//                }
//                .flatMap { loadFromDb() }
//        }
//
//        result = when {
//            context.isNetworkStatusAvailable() -> networkObservable
//                .map<Resource<ResultType>> { Resource.success(it) }
//                .onErrorReturn { Resource.error(it) }
//                // Read results in Android Main Thread (UI)
//                .observeOn(AndroidSchedulers.mainThread())
//                .startWith(Resource.loading())
//            else -> diskObservable
//                .map<Resource<ResultType>> { Resource.success(it) }
//                .onErrorReturn { Resource.error(it) }
//                // Read results in Android Main Thread (UI)
//                .observeOn(AndroidSchedulers.mainThread())
//                .startWith(Resource.loading())
//        }
//    }
//
//    fun asFlowable(): Single<Resource<ResultType>> {
//        return result
//    }
//
//    private fun processResponse(response: Resource<RequestType>): RequestType {
//        return response.data!!
//    }
//
//    protected abstract fun saveCallResult(request: RequestType)
//
//    protected abstract fun loadFromDb(): Single<ResultType>
//
//    protected abstract fun createCall(): Single<Resource<RequestType>>
//}