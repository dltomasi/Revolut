package com.revolut

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.revolut.rate.model.Rate
import com.revolut.ui.MainActivity
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest
import androidx.fragment.app.testing.launchFragmentInContainer
import com.revolut.ui.rate.RatesFragment


@RunWith(AndroidJUnit4::class)
class RateFragmentTest : KoinTest {

    private lateinit var rateRobot: RateRobot

    @Before
    fun setUpTests() {

        rateRobot = RateRobot()
        StandAloneContext.loadKoinModules(rateRobot.koinModule())

        launchFragmentInContainer<RatesFragment>(null, R.style.AppTheme)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.revolut", appContext.packageName)

        rateRobot {
            setUp {
                checkItem()
            }
        }


    }

}
