package com.revolut.country.background

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.revolut.country.model.Country
import com.revolut.country.model.CountryResponse
import com.revolut.country.network.CountryService
import com.revolut.country.persistence.CountryPersistence
import io.reactivex.Single
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class CountryWorkManager(
    context: Context,
    params: WorkerParameters
) : RxWorker(context, params), KoinComponent {


    private val countryService: CountryService by inject()
    private val countryPersistence: CountryPersistence by inject()

    override fun createWork(): Single<Result> {
        val base = inputData.getString("base")!!
        return countryService.fetchCountry(base).singleOrError()
            .map { countryPersistence.set(base, getCorrectCountry(base, it)) }
            .map { Result.success() }
    }

    private fun getCorrectCountry(base: String, countries: List<CountryResponse>): Country {
        return when (base) {
            "EUR" -> Country("Euro", "https://restcountries.eu/data/gbr.svg")
            "GBP" -> Country("British pound", "https://restcountries.eu/data/gbr.svg")
            "AUD" -> Country(countries[1].currencies[0].name, countries[1].flag)
            "USD" -> Country(countries[18].currencies[0].name, countries[18].flag)
            else -> Country(countries[0].currencies[0].name, countries[0].flag)
        }
    }
}