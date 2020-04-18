package com.revolut

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// example of kotlin extensions

fun <T> Observable<T>.uiSubscribe(schedulers: SchedulersProvider): Observable<T> {
    return subscribeOn(schedulers.background).observeOn(schedulers.main)
}

fun <T> Observable<T>.backgroundSubscribe(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
}
