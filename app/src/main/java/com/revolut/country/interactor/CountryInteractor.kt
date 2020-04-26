package com.revolut.country.interactor


import com.revolut.country.model.Country
import io.reactivex.Observable

interface CountryInteractor {

    fun fetchCountry(base: String): Observable<Country>
}