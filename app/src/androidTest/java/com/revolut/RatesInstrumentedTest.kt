package com.revolut

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.revolut.ui.rate.RatesFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest


@RunWith(AndroidJUnit4::class)
class RateFragmentTest : KoinTest {

    private val mockedModules = RateRobot().koinModule()

    @Before
    fun setUpTests() {
        StandAloneContext.loadKoinModules(mockedModules)
        launchFragmentInContainer<RatesFragment>(null, R.style.AppTheme)
    }

    @Test
    fun click_on_item_should_make_it_first() {
        start {
            checkItem(0, "EUR", "1.00")
            clickItem(2)
            checkItem(0, "Currency 2", "3.00")
        }
    }

    @Test
    fun edit_value_should_update_others() {
        start {
            editItem("1.00" ,"2.00")
            checkItem(0, "EUR", "2.00")
            checkItem(1, "Currency 1", "1.00")
            checkItem(2, "Currency 2", "6.00")
        }
    }

}
