package com.revolut.model

import com.revolut.rx.RatesMap

data class RatesResponse(
    val baseCurrency: String,
    val rates: RatesMap
)
