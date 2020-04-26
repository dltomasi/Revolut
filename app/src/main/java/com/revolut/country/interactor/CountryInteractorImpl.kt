package com.revolut.country.interactor


import com.revolut.country.model.Country
import com.revolut.country.network.CountryService
import io.reactivex.Observable

class CountryInteractorImpl(private val countryService: CountryService) :
    CountryInteractor {

    override fun fetchCountry(base: String): Observable<Country> =
        countryService.fetchCountry(base)
            .map { it[0] }

}