package com.revolut.rate.interactor


import com.revolut.rate.model.Rate
import com.revolut.rate.network.RateService
import io.reactivex.Observable

class RatesInteractorImpl(private val rateService: RateService) :
    RatesInteractor {

    override fun fetchRates(base: String): Observable<List<Rate>> =
        rateService.fetchRates(base)
            .map { it.rates.toList() }
            .flatMapIterable { it }
            .map { Rate(it.first, it.second, null) }
            .toList().toObservable()
}