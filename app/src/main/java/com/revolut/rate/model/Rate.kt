package com.revolut.rate.model

class Rate(
    val currency: String,
    var rate: Double,
    var flag: String? = null
) {

    fun rateText(): String = "%.2f".format(rate)

    fun rateValue(base: Rate): Double =
        this.rate * base.rate

    override fun equals(other: Any?): Boolean {
        other as Rate
        return this.currency == other.currency
                && this.rate == other.rate
    }
}


