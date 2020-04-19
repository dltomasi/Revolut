package com.revolut.ui.main

import com.revolut.RatesMap
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RatesAdapterTest {

    private lateinit var adapter: RatesAdapter
    private var rates: RatesMap = RatesMap()

    init {
        rates["r1"] = 1F
        rates["r2"] = 2F
    }

    @Before
    fun before() {
        adapter = RatesAdapter()
    }

    @Test
    fun `set data should set items`() {
        assertEquals(adapter.itemCount, 2)
    }
}