package com.revolut.network

import com.revolut.model.RatesResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface DataService {

    @GET("latest?base=EUR")
    fun fetchRates() : Observable<RatesResponse>

}