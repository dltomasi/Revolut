package com.revolut.interactor


import com.revolut.model.Rates
import io.reactivex.Observable

interface RatesInteractor {

    fun fetchRates(): Observable<Rates>
}