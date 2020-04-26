package com.revolut.rate.interactor


import com.revolut.rx.RatesMap
import com.revolut.rate.network.RateService
import io.reactivex.Observable

class RatesInteractorImpl(private val rateService: RateService) :
    RatesInteractor {

    override fun fetchRates(base: String): Observable<RatesMap> =
        rateService.fetchRates(base)
            .map { it.rates }
}