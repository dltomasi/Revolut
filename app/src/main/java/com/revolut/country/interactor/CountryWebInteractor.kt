package com.revolut.country.interactor


import com.revolut.country.model.Country
import io.reactivex.Single

interface CountryWebInteractor {

    fun fetchCountry(base: String): Single<Country>
}