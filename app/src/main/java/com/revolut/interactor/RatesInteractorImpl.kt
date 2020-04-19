package com.revolut.interactor


import com.revolut.RatesMap
import com.revolut.network.DataService
import io.reactivex.Observable

class RatesInteractorImpl(private val dataService: DataService) : RatesInteractor {

    override fun fetchRates(): Observable<RatesMap> =
        dataService.fetchRates()
            .map { it.rates }
}