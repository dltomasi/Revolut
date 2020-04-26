package com.revolut.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.revolut.rate.model.Rate
import com.revolut.ui.rate.RatesAdapter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@Ignore
class RatesAdapterTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var adapter: RatesAdapter
    private var rates = listOf(Rate("r1" ,1.0), Rate("r2", 2.0))

    @Before
    fun before() {
        adapter = RatesAdapter()
    }

    @Test
    fun `set data should set items`() {
        adapter.setData(rates)
        assertEquals(adapter.itemCount, 2)
    }
}