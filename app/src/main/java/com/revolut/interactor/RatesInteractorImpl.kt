package com.revolut.interactor


import com.revolut.model.Rates
import com.revolut.network.DataService
import io.reactivex.Observable

class RatesInteractorImpl(private val dataService: DataService) : RatesInteractor {

    override fun fetchRates(): Observable<Rates> =
        dataService.fetchRates()
            .map { it.rates }
}