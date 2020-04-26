package com.revolut.interactor

import com.google.gson.internal.LinkedTreeMap
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.revolut.rate.interactor.RatesInteractor
import com.revolut.rate.interactor.RatesInteractorImpl
import com.revolut.rx.RatesMap
import com.revolut.rate.network.RatesResponse
import com.revolut.rate.network.RateService
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class RatesInteractorTest {

    private lateinit var subject: RatesInteractor

    private val mockRateService: RateService = mock()
    var ratesMap: RatesMap = LinkedTreeMap()
    private var responseData: RatesResponse

    init {
        ratesMap["r1"] = 1.0
        ratesMap["r2"] = 2.0
        responseData = RatesResponse(
            "base",
            ratesMap
        )
    }

    @Before
    fun before(){
        whenever(mockRateService.fetchRates(any())).thenReturn(Observable.just(responseData))
        subject =
            RatesInteractorImpl(mockRateService)
    }

    @Test
    fun `fetch data should succeed`(){
        subject.fetchRates("").test()
            .assertValue(responseData.rates)
    }
}