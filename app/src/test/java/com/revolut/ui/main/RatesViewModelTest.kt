package com.revolut.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.internal.LinkedTreeMap
import com.nhaarman.mockito_kotlin.*
import com.revolut.RatesMap
import com.revolut.TestSchedulerProvider
import com.revolut.interactor.RatesInteractor
import com.revolut.ui.main.RatesViewModel.Companion.START_CURRENCY
import com.revolut.ui.main.RatesViewModel.Companion.TIME_INTERVAL
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
        whenever(ratesInteractor.fetchRates(any())).thenReturn(Observable.just(ratesMap))
        viewModel = RatesViewModel(schedulersProvider, ratesInteractor)
    }

    @Test
    fun `should get rates on init`() {
        testScheduler.triggerActions()
        verify(ratesInteractor, times(1)).fetchRates(any())
        val expected = ratesMap.toList().toMutableList()
        expected.add(0, START_CURRENCY)
        assertEquals(expected, viewModel.rates.value)
    }

    @Test
    fun `should get rates every 1 sec`() {
        testScheduler.triggerActions()
        verify(ratesInteractor, times(1)).fetchRates(any())
        testScheduler.advanceTimeBy(TIME_INTERVAL, TimeUnit.SECONDS)
        verify(ratesInteractor, times(2)).fetchRates(any())
        testScheduler.advanceTimeBy(TIME_INTERVAL, TimeUnit.SECONDS)
        verify(ratesInteractor, times(3)).fetchRates(any())
    }
}