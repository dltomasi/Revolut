package com.revolut

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider(private val scheduler: TestScheduler) : SchedulersProvider {
    override val main: Scheduler
        get() = scheduler
    override val background: Scheduler
        get() = scheduler

}