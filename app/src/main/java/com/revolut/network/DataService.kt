package com.revolut.network

import com.revolut.model.RatesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface DataService {

    @GET("latest")
    fun fetchRates(@Query("base") one: String) : Observable<RatesResponse>

}