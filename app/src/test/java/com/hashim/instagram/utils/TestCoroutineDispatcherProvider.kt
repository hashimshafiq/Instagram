package com.hashim.instagram.utils

import com.hashim.instagram.utils.rx.CoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler

@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutineDispatcherProvider(private val testCoroutineScheduler: TestCoroutineScheduler) :
    CoroutineDispatchers {

    override fun default(): TestCoroutineDispatcher = TestCoroutineDispatcher(testCoroutineScheduler)

    override fun io(): TestCoroutineDispatcher = TestCoroutineDispatcher(testCoroutineScheduler)

    override fun main(): TestCoroutineDispatcher = TestCoroutineDispatcher(testCoroutineScheduler)
}