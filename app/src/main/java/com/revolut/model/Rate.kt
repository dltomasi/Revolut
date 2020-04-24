package com.revolut.model

typealias Rate = Pair<String, Float>

fun Rate.currency(): String =
    this.first

fun Rate.rateText(): String =
    this.second.toString()

fun Rate.rateValue(): Float =
    this.second

fun Rate.rateValue(base: Rate): Float =
    this.rateValue()*base.rateValue()