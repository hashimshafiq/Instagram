package com.hashim.instagram.utils

import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider(private val testScheduler: TestScheduler) : SchedulerProvider {

    override fun computation() : Scheduler = testScheduler

    override fun io() : Scheduler = testScheduler

    override fun ui() : Scheduler = testScheduler


}