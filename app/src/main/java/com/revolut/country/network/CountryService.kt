package com.revolut.country.network

import com.revolut.country.model.Country
import com.revolut.country.model.CountryResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryService {

    @GET("currency/{value}")
    fun fetchCountry(@Path("value") currency: String) : Observable<List<CountryResponse>>

}