package com.revolut.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.revolut.country.interactor.CountryInteractor
import com.revolut.country.interactor.CountryInteractorImpl
import com.revolut.country.model.Country
import com.revolut.country.model.CountryResponse
import com.revolut.country.model.CurrencyResponse
import com.revolut.country.network.CountryService
import com.revolut.country.persistence.CountryPersistence
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class CountryInteractorTest {

    private lateinit var subject: CountryInteractor

    private val mockCountryService: CountryService = mock()
    private val mockCountryPersistence: CountryPersistence = mock()
    private val c1 = CountryResponse(listOf(CurrencyResponse("real")),"flag1")
    private val c2 = CountryResponse(listOf(CurrencyResponse("c2")),"flag2")
    private var countries = listOf(c1, c2)


    @Before
    fun before() {
        whenever(mockCountryService.fetchCountry("brl")).thenReturn(Observable.just(countries))
        subject =
            CountryInteractorImpl(mockCountryService, mockCountryPersistence)
    }

    @Test
    fun `fetch data should succeed`() {
        subject.getCountry("brl").test()
            .assertValue(Country("real", "flag1"))
    }
}