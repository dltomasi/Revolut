package com.revolut.interactor


import com.revolut.RatesMap
import io.reactivex.Observable

interface RatesInteractor {

    fun fetchRates(base: String): Observable<RatesMap>
}