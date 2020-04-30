package com.revolut.interactor

import com.google.gson.internal.LinkedTreeMap
import com.nhaarman.mockito_kotlin.*
import com.revolut.rate.interactor.RatesInteractor
import com.revolut.rate.interactor.RatesInteractorImpl
import com.revolut.rate.model.Rate
import com.revolut.RatesMap
import com.revolut.country.interactor.CountryInteractor
import com.revolut.country.model.Country
import com.revolut.rate.network.RatesResponse
import com.revolut.rate.network.RateService
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class RatesInteractorTest {

    private lateinit var subject: RatesInteractor

    private val rateService: RateService = mock()
    private val countryInteractor: CountryInteractor = mock()
    var ratesMap: RatesMap = LinkedTreeMap()
    private var responseData: RatesResponse

    private val c1 = Country("c1", "f1")
    private val c2 = Country("c2", "f2")
    private val rates = listOf(Rate("r1", 1.0, c1), Rate("r2", 2.0, c2))

    init {
        ratesMap["r1"] = 1.0
        ratesMap["r2"] = 2.0
        responseData = RatesResponse(
            "base",
            ratesMap
        )
    }

    @Before
    fun before() {
        whenever(rateService.fetchRates(any())).thenReturn(Observable.just(responseData))
        whenever(countryInteractor.getCountry("r1")).thenReturn(Single.just(c1))
        whenever(countryInteractor.getCountry("r2")).thenReturn(Single.just(c2))
        subject = RatesInteractorImpl(rateService, countryInteractor)
    }

    @Test
    fun `fetch data should succeed`() {
        subject.fetchRates("").test()
            .assertValue(rates)
    }

    @Test
    fun `should get country info for each currency`() {
        subject.fetchRates("").test()
        verify(countryInteractor).getCountry("r1")
        verify(countryInteractor).getCountry("r2")
        verifyNoMoreInteractions(countryInteractor)
    }
}