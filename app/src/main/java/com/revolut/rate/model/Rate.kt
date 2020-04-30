package com.revolut.rate.model

import com.revolut.country.model.Country

class Rate(
    val currency: String,
    var rate: Double,
    var country: Country? = null
) {

    fun rateText(): String = "%.2f".format(rate)

    fun rateValue(base: Rate): Double =
        this.rate * base.rate

    override fun equals(other: Any?): Boolean {
        other as Rate
        return this.currency == other.currency
                && this.rate == other.rate
                && this.country?.equals(other.country) ?: true
    }

    override fun toString(): String {
        return "$currency: ${rateText()}"
    }
}

fun List<Rate>.copy(): MutableList<Rate> {
    return map { Rate(it.currency, it.rate, it.country) }.toMutableList()
}



