package com.revolut.rate.network

import com.revolut.RatesMap

data class RatesResponse(
    val baseCurrency: String,
    val rates: RatesMap
)
