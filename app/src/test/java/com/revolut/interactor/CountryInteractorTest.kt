package com.revolut.interactor

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.nhaarman.mockito_kotlin.*
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

    private val countryPersistence: CountryPersistence = mock()
    private val workManager: WorkManager = mock()
    private val c1 = Country("c1", "flag1")


    @Before
    fun before() {
        whenever(countryPersistence.get("persisted")).thenReturn(c1)
        subject = CountryInteractorImpl(countryPersistence, workManager)
    }

    @Test
    fun `persisted value should return value and not call worker`() {
        subject.getCountry("persisted").test()
            .assertValue(Country("c1", "flag1"))

        verifyNoMoreInteractions(workManager)
    }

    @Test
    fun `not persisted value should return empty and call worker`() {
        subject.getCountry("not persisted").test()
            .assertValue(Country.EMPTY)

        verify(workManager).enqueue(any< OneTimeWorkRequest>())
    }
}