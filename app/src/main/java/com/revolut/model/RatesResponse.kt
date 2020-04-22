package com.revolut.model


import com.revolut.RatesMap

data class RatesResponse(

    val baseCurrency: String,
    val rates: RatesMap
)

typealias Rate = Pair<String, Float>

fun Rate.getCurrency(): String =
    this.first

fun Rate.getRate(): String =
     this.second.toString()

fun Rate.getRateValue(): Float =
    this.second
