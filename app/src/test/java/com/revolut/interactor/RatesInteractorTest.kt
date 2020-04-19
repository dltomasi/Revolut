package com.revolut.interactor

import com.google.gson.internal.LinkedTreeMap
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.revolut.RatesMap
import com.revolut.model.RatesResponse
import com.revolut.network.DataService
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class RatesInteractorTest {

    private lateinit var subject: RatesInteractor

    private val mockDataService: DataService = mock()
    var ratesMap: RatesMap = LinkedTreeMap()
    private var responseData: RatesResponse

    init {
        ratesMap["r1"] = 1F
        ratesMap["r2"] = 2F
        responseData = RatesResponse(
            "base",
            ratesMap)
    }

    @Before
    fun before(){
        whenever(mockDataService.fetchRates()).thenReturn(Observable.just(responseData))
        subject = RatesInteractorImpl(mockDataService)
    }

    @Test
    fun `fetch data should succeed`(){
        subject.fetchRates().test()
            .assertValue(responseData.rates)
    }
}