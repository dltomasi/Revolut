package com.revolut.rate.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RateService {

    @GET("latest")
    fun fetchRates(@Query("base") one: String) : Observable<RatesResponse>

}