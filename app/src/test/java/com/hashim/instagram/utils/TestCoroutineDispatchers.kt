package com.hashim.instagram.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
interface TestCoroutineDispatchers {

    fun computation(): TestCoroutineDispatcher

    fun io(): TestCoroutineDispatcher

    fun main(): TestCoroutineDispatcher
}