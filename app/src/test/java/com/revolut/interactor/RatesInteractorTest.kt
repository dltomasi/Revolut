package com.revolut.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.revolut.model.Rates
import com.revolut.model.RatesResponse
import com.revolut.network.DataService
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class RatesInteractorTest {

    private lateinit var subject: RatesInteractor

    private val mockDataService: DataService = mock()
    private val responseData: RatesResponse = RatesResponse(
        "base",
        Rates(hashMapOf("test1" to 1F, "test2" to 2f))
    )

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