package com.revolut

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.revolut.country.interactor.CountryInteractor
import com.revolut.country.model.Country
import com.revolut.rate.interactor.RatesInteractor
import com.revolut.rate.model.Rate
import com.revolut.ui.rate.RatesViewModel
import com.revolut.utils.IdlingResourceViewActions
import com.revolut.utils.withRecyclerView
import io.reactivex.Observable
import io.reactivex.Single
import org.hamcrest.Matchers.allOf
import org.koin.dsl.module.module


class RateRobot {

    companion object {
        val mockedList = listOf(Rate("Currency 1", 0.5), Rate("Currency 2", 3.0))
    }

    inner class SetUp {

        private val ratesInteractor: RatesInteractor = mock {
            whenever(it.fetchRates(RatesViewModel.START_CURRENCY)) doReturn Observable.just(mockedList)
        }

        private val countryInteractor: CountryInteractor = mock {
            whenever(it.getCountry("EUR")) doReturn Single.just(Country.EMPTY)
        }

        var mockKoinModule = module(override = true) {
            factory { ratesInteractor }
            factory { countryInteractor }
        }
    }

    internal fun waitForRateListInit() {
        onView(withId(R.id.main))
            .perform(IdlingResourceViewActions.fromViewPredicate<View> {
                findViewById<RecyclerView>(R.id.rates_list)?.let { it.childCount != 0 } ?: false
            })
    }

    fun checkItem(position: Int, currency: String, value: String) {
        onView(withRecyclerView(R.id.rates_list)
            .atPosition(position))
            .check(matches(hasDescendant(withText(currency))))
            .check(matches(hasDescendant(withText(value))))
    }

    fun clickItem(position: Int) {
        onView(withRecyclerView(R.id.rates_list)
            .atPosition(position))
            .perform(click())
    }

    fun editItem(oldValue: String, newValue: String) {
        onView(
            allOf(withText(oldValue)))
            .perform(replaceText(newValue))
    }
}

fun start(func: RateRobot.() -> Unit) = RateRobot().apply {
    waitForRateListInit()
    func()
}

fun RateRobot.koinModule() = this.SetUp().mockKoinModule
