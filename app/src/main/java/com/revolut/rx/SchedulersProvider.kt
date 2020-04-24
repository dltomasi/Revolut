package com.revolut.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulersProvider {
    val main: Scheduler
    val background: Scheduler

    class Impl : SchedulersProvider {
        override val main: Scheduler
            get() = AndroidSchedulers.mainThread()
        override val background: Scheduler
            get() = Schedulers.io()
    }
}
