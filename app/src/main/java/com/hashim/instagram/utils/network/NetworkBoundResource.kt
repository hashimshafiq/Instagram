package com.hashim.instagram.utils.network


import com.hashim.instagram.utils.common.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType?>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType?) -> Boolean,
    coroutineDispatcher: CoroutineDispatcher
) = flow<Resource<ResultType>> {

    // check for data in database
    val data = query().firstOrNull()

    if (data != null) {
        // data is not null -> update loading status
        emit(Resource.loading(data))
    }

    if (shouldFetch(data)) {
        // Need to fetch data -> call backend
        val fetchResult = fetch()
        // got data from backend, store it in database
        saveFetchResult(fetchResult)
    }

    // load updated data from database (must not return null anymore)
    val updatedData = query().first()

    // emit updated data
    emit(Resource.success(updatedData))

}.onStart {
    emit(Resource.loading(null))

}.catch { exception ->

    emit(Resource.error(null))

}.flowOn(coroutineDispatcher)
