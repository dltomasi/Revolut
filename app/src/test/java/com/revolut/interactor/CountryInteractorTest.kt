package com.revolut.interactor

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.revolut.country.interactor.CountryInteractor
import com.revolut.country.interactor.CountryInteractorImpl
import com.revolut.country.model.Country
import com.revolut.country.network.CountryService
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class CountryInteractorTest {

    private lateinit var subject: CountryInteractor

    private val mockCountryService: CountryService = mock()
    private val c1 = Country("flag1")
    private val c2 = Country("flag2")
    var countries = listOf(c1, c2)


    @Before
    fun before() {
        whenever(mockCountryService.fetchCountry(any())).thenReturn(Observable.just(countries))
        subject =
            CountryInteractorImpl(mockCountryService)
    }

    @Test
    fun `fetch data should succeed`() {
        subject.fetchCountry("brl").test()
            .assertValue(countries[0])
    }
}