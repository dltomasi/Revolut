package com.revolut.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.internal.LinkedTreeMap
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.revolut.RatesMap
import com.revolut.TestSchedulerProvider
import com.revolut.interactor.RatesInteractor
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.TimeUnit


class RatesViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: RatesViewModel

    private val ratesInteractor: RatesInteractor = mock()
    private val testScheduler = TestScheduler()
    private val schedulersProvider = TestSchedulerProvider(testScheduler)
    var ratesMap: RatesMap = LinkedTreeMap()

    init {
        ratesMap["r1"] = 1F
        ratesMap["r2"] = 2F
    }
    @Before
    fun before() {
        whenever(ratesInteractor.fetchRates()).thenReturn(Observable.just(ratesMap))
        viewModel = RatesViewModel(schedulersProvider, ratesInteractor)
    }

    @Test
    fun `should get rates on init`() {
        testScheduler.triggerActions()
        verify(ratesInteractor, times(1)).fetchRates()
        assertEquals(ratesMap, viewModel.rates.value)
    }

    @Test
    fun `should get rates every 1 sec`() {
        testScheduler.triggerActions()
        verify(ratesInteractor, times(1)).fetchRates()
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        verify(ratesInteractor, times(2)).fetchRates()
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        verify(ratesInteractor, times(3)).fetchRates()
    }
}