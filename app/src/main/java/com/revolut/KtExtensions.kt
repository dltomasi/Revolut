package com.revolut

import com.google.gson.internal.LinkedTreeMap
import com.revolut.rx.SchedulersProvider
import io.reactivex.Observable

typealias RatesMap = LinkedTreeMap<String, Double>

fun <T> Observable<T>.uiSubscribe(schedulers: SchedulersProvider): Observable<T> {
    return subscribeOn(schedulers.background).observeOn(schedulers.main)
}

fun <T> Observable<T>.backgroundSubscribe(schedulers: SchedulersProvider): Observable<T> {
    return subscribeOn(schedulers.background).observeOn(schedulers.background)
}
