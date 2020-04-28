package com.revolut.country.interactor


import android.util.Log
import com.revolut.country.model.Country
import com.revolut.country.model.CountryResponse
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
                .map { getCorrectCountry(base, it) }
                .doOnNext { countryPersistence.set(base, it) }
        }

    private fun getCorrectCountry(base:String, countries: List<CountryResponse>) : Country {
       return when (base) {
            "EUR" -> Country("Euro", "https://restcountries.eu/data/gbr.svg")
            "GBP" -> Country("British pound", "https://restcountries.eu/data/gbr.svg")
            "AUD" -> Country(countries[1].currencies[0].name, countries[1].flag)
            "USD" -> Country(countries[18].currencies[0].name, countries[18].flag)
            else -> Country(countries[0].currencies[0].name, countries[0].flag)
        }
    }

}