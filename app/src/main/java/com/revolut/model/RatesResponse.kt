package com.revolut.model

import com.google.gson.internal.LinkedTreeMap
import com.revolut.RatesMap

data class RatesResponse(

    val baseCurrency: String,
    val rates: RatesMap
)

class Rates(val underlying:LinkedTreeMap<String, Float>) : Map<String, Float> by underlying {
    override fun get(key: String): Float? {
        return underlying[key]
    }
}

fun Rates.getRateFor(currency:String): Float? =
     this.underlying[currency]

fun Rates.getCurrencies() : List<String> = this.keys.toList()
