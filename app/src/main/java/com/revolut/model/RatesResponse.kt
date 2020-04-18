package com.revolut.model

data class RatesResponse(

    val baseCurrency: String,
    val rates: Rates
)

class Rates(val underlying:HashMap<String, Float>) : MutableMap<String, Float> by underlying {
    override fun get(key: String): Float? {
        return underlying[key]
    }
}

fun Rates.getRateFor(currency:String): Float? =
     this.underlying[currency]

fun Rates.getCurrencies() : List<String> = this.keys.toList()
