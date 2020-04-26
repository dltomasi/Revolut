package com.revolut.rate.network

import com.revolut.rx.RatesMap

data class RatesResponse(
    val baseCurrency: String,
    val rates: RatesMap
)
