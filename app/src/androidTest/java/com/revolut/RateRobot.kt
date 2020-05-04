package com.revolut

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.revolut.country.interactor.CountryInteractor
import com.revolut.country.model.Country
import com.revolut.rate.interactor.RatesInteractor
import com.revolut.rate.model.Rate
import io.reactivex.Observable
import io.reactivex.Single
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

class RateRobot {

    val mockedList = listOf(Rate("Currency 1", 1.0), Rate("Currency 2", 2.0))

    var setUp = SetUp()

    fun setUp(func: SetUp.() -> Unit) {
        setUp.apply {
            waitForRateListInit()
            func()
        }
    }

    inner class SetUp {

        private val ratesInteractor: RatesInteractor = mock {
            whenever(it.fetchRates("EUR")) doReturn Observable.just(mockedList)
        }

        private val countryInteractor: CountryInteractor = mock {
            whenever(it.getCountry("EUR")) doReturn Single.just(Country.EMPTY)
        }

        var mockKoinModule = module(override = true) {
            factory { ratesInteractor }
            single { countryInteractor }
        }

        internal fun waitForRateListInit() {
            onView(withId(R.id.main))
                .perform(IdlingResourceViewActions.fromViewPredicate<View> {
                    findViewById<RecyclerView>(R.id.rates_list)?.let { it.childCount != 0 } ?: false
                })
        }
    }

    fun checkItem() {
        onView(withId(R.id.rates_list))
            .check(ViewAssertions.matches(atPosition(0, (ViewMatchers.withText("EUR")))));
    }

    operator fun invoke(func: RateRobot.() -> Unit) = func()

}

fun RateRobot.koinModule() = this.setUp.mockKoinModule


fun atPosition(position: Int, itemMatcher: Matcher<View>?): Matcher<View>? {
    checkNotNull(itemMatcher)
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position)
                ?: // has no item on such position
                return false
            return itemMatcher.matches(viewHolder.itemView)
        }
    }
}
