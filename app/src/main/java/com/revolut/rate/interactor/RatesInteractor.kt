package com.revolut.rate.interactor


import com.revolut.rate.model.Rate
import io.reactivex.Observable

interface RatesInteractor {

    fun fetchRates(base: String): Observable<List<Rate>>
}