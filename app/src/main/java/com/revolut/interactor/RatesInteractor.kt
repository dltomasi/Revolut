package com.revolut.interactor


import com.revolut.model.RatesResponse
import io.reactivex.Observable

interface RatesInteractor {

    fun fetchRates(): Observable<RatesResponse>
}