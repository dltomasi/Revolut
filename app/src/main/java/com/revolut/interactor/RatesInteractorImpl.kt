package com.revolut.interactor


import com.revolut.RatesMap
import com.revolut.network.DataService
import io.reactivex.Observable

class RatesInteractorImpl(private val dataService: DataService) : RatesInteractor {

    override fun fetchRates(base: String): Observable<RatesMap> =
        dataService.fetchRates(base)
            .map { it.rates }
}