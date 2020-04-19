package com.revolut

import com.google.gson.internal.LinkedTreeMap
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

typealias RatesMap = LinkedTreeMap<String, Float>

fun <T> Observable<T>.uiSubscribe(schedulers: SchedulersProvider): Observable<T> {
    return subscribeOn(schedulers.background).observeOn(schedulers.main)
}

fun <T> Observable<T>.backgroundSubscribe(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
}
