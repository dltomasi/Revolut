package com.revolut.country.interactor


import com.revolut.country.model.Country
import io.reactivex.Single

interface CountryInteractor {

    fun getCountry(base: String): Single<Country>
}