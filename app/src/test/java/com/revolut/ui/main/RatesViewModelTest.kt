package com.revolut.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import com.revolut.TestSchedulerProvider
import com.revolut.country.interactor.CountryInteractor
import com.revolut.rate.interactor.RatesInteractor
import com.revolut.rate.model.Rate
import com.revolut.ui.rate.RatesViewModel
import com.revolut.ui.rate.RatesViewModel.Companion.START_CURRENCY
import com.revolut.ui.rate.RatesViewModel.Companion.TIME_INTERVAL
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.TimeUnit


class RatesViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: RatesViewModel

    private val ratesInteractor: RatesInteractor = mock()
    private val countryInteractor: CountryInteractor = mock()
    private val testScheduler = TestScheduler()
    private val schedulersProvider = TestSchedulerProvider(testScheduler)
    private val ratesEur = listOf(Rate("r1", 1.0), Rate("r2", 2.0))
    private val ratesR1 = listOf(Rate("EUR", 0.5), Rate("r2", 2.0))


    @Before
    fun before() {
        whenever(ratesInteractor.fetchRates("EUR")).thenReturn(Observable.just(ratesEur))
        whenever(ratesInteractor.fetchRates("r1")).thenReturn(Observable.just(ratesR1))
        whenever(ratesInteractor.fetchRates("r2")).thenReturn(Observable.error(Exception()))
        whenever(countryInteractor.getCountry(any())).thenReturn(Single.just(mock()))
        viewModel = RatesViewModel(
            schedulersProvider,
            ratesInteractor,
            countryInteractor
        )
        viewModel.onStart()
        testScheduler.triggerActions()
    }

    @Test
    fun `should get rates on start`() {
        verify(ratesInteractor, times(1)).fetchRates(any())
        val expected = ratesEur.toList().toMutableList()
        expected.add(0, START_CURRENCY)
        assertEquals(expected, viewModel.rates.value)
    }

    @Test
    fun `should get rates every 1 sec`() {
        verify(ratesInteractor, times(1)).fetchRates(any())
        testScheduler.advanceTimeBy(TIME_INTERVAL, TimeUnit.SECONDS)
        verify(ratesInteractor, times(2)).fetchRates(any())
        testScheduler.advanceTimeBy(TIME_INTERVAL, TimeUnit.SECONDS)
        verify(ratesInteractor, times(3)).fetchRates(any())
    }

    @Test
    @Ignore
    fun `set empty value should update list with zeros`() {
        viewModel.setNewValue("a")

        val expected = listOf(Rate("EUR", 0.0), Rate("r1", 0.0), Rate("r2", 0.0))
        assertTrue(expected == viewModel.rates.value!!)
    }

    @Test
    fun `set new value should update list`() {
        viewModel.setNewValue("2")

        val expected = listOf(Rate("EUR", 2.0), Rate("r1", 2.0), Rate("r2", 4.0))
        assertEquals(expected, viewModel.rates.value!!)
    }

    @Test
    fun `select new item should update list with new rates`() {
        viewModel.selectItem(1)
        testScheduler.triggerActions()
        testScheduler.advanceTimeBy(TIME_INTERVAL, TimeUnit.SECONDS)
        testScheduler.triggerActions()

        val expected = listOf(Rate("r1", 1.0), Rate("EUR", 0.5), Rate("r2", 2.0))
        assertEquals(expected, viewModel.rates.value!!)
    }

    @Test
    fun `on error should display error screen`() {
        whenever(ratesInteractor.fetchRates("EUR")).thenReturn(Observable.error(Exception()))
        viewModel.selectItem(2)
        testScheduler.advanceTimeBy(TIME_INTERVAL, TimeUnit.SECONDS)
        testScheduler.triggerActions()

        assertTrue(viewModel.error.value!!)
    }


}