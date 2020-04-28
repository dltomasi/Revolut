package com.revolut.country.interactor


import android.util.Log
import com.revolut.country.model.Country
import com.revolut.country.network.CountryService
import com.revolut.country.persistence.CountryPersistence
import io.reactivex.Observable

class CountryInteractorImpl(
    private val countryService: CountryService,
    private val countryPersistence: CountryPersistence
) : CountryInteractor {

    override fun getCountry(base: String): Observable<Country> =
        countryPersistence.get(base)?.let {
            Observable.just(it)
        } ?: run {
            //Log.d("country", base)
            countryService.fetchCountry(base)
                .filter { it.isNotEmpty() }
                .map { it[0] }
                .doOnNext{countryPersistence.set(base, it)}
        }

}