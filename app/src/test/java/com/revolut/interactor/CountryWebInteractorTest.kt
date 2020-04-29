package com.revolut.interactor

import androidx.work.WorkManager
import com.nhaarman.mockito_kotlin.*
import com.revolut.country.interactor.CountryInteractor
import com.revolut.country.interactor.CountryInteractorImpl
import com.revolut.country.interactor.CountryWebInteractor
import com.revolut.country.interactor.CountryWebInteractorImpl
import com.revolut.country.model.Country
import com.revolut.country.model.CountryResponse
import com.revolut.country.model.CurrencyResponse
import com.revolut.country.network.CountryService
import com.revolut.country.persistence.CountryPersistence
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class CountryWebInteractorTest {

    private lateinit var subject: CountryWebInteractor

    private val countryPersistence: CountryPersistence = mock()
    private val countryService: CountryService = mock()
    private val c1 = CountryResponse(listOf(CurrencyResponse("real")),"flag1")
    private val c2 = CountryResponse(listOf(CurrencyResponse("c2")),"flag2")
    private var countries = listOf(c1, c2)


    @Before
    fun before() {
        whenever(countryService.fetchCountry("brl")).thenReturn(Single.just(countries))
        subject = CountryWebInteractorImpl(countryService, countryPersistence)
    }

    @Test
    fun `fetch country should persist`() {
        subject.fetchCountry("brl").test()
            .assertValue(Country("real", "flag1"))

        verify(countryPersistence).set(eq("brl"), any())
    }
}