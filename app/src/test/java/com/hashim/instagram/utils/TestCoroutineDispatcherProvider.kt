package com.hashim.instagram.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutineDispatcherProvider() : TestCoroutineDispatchers {

    override fun computation(): TestCoroutineDispatcher = TestCoroutineDispatcher()

    override fun io(): TestCoroutineDispatcher = TestCoroutineDispatcher()

    override fun main(): TestCoroutineDispatcher = TestCoroutineDispatcher()
}