package com.revolut.rate.model

typealias Rate = Pair<String, Double>

fun Rate.currency(): String =
    this.first

fun Rate.rateText(): String =
    "%.2f".format(this.second)

fun Rate.rateValue(): Double =
    this.second

fun Rate.rateValue(base: Rate): Double =
    this.rateValue()*base.rateValue()