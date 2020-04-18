package com.revolut.interactor


import com.revolut.model.RatesResponse
import com.revolut.network.DataService
import io.reactivex.Observable

class RatesInteractorImpl(private val dataService: DataService) : RatesInteractor {

    override fun fetchRates(): Observable<RatesResponse> {
        return dataService.fetchRates()
    }

}