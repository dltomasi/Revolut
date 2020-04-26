package com.revolut.rate.interactor


import com.revolut.rx.RatesMap
import io.reactivex.Observable

interface RatesInteractor {

    fun fetchRates(base: String): Observable<RatesMap>
}