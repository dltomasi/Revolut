package com.revolut.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.revolut.SchedulersProvider
import com.revolut.interactor.RatesInteractor
import com.revolut.model.Rates
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class RatesViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: RatesViewModel

    private val ratesInteractor: RatesInteractor = mock()
    private val schedulersProvider: SchedulersProvider = mock()
    private val responseData: Rates = Rates(
        hashMapOf("test1" to 1F, "test2" to 2f)
    )

    @Before
    fun before() {
        whenever(schedulersProvider.background).thenReturn(Schedulers.trampoline())
        whenever(schedulersProvider.main).thenReturn(Schedulers.trampoline())
        whenever(ratesInteractor.fetchRates()).thenReturn(Observable.just(responseData))
        viewModel = RatesViewModel(schedulersProvider, ratesInteractor)
    }

    @Test
    fun `should get rates on init`() {
        assertEquals(viewModel.rates.value, responseData)
    }

}