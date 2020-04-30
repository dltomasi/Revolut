package com.revolut.rate.interactor


import com.revolut.country.interactor.CountryInteractor
import com.revolut.rate.model.Rate
import com.revolut.rate.network.RateService
import io.reactivex.Observable

class RatesInteractorImpl(
    private val rateService: RateService,
    private val countryInteractor: CountryInteractor
) : RatesInteractor {

    override fun fetchRates(base: String): Observable<List<Rate>> =
        rateService.fetchRates(base)
            .map { it.rates.toList() }
            .flatMapIterable { it }
            .map { Rate(it.first, it.second, null) }
            .flatMapSingle { rate ->
                countryInteractor.getCountry(rate.currency)
                    .map {
                        rate.country = it
                        rate
                    }
            }
            .toList().toObservable()
}