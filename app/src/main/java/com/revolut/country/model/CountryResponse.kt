package com.revolut.country.model


data class Country(
    val name: String,
    val flag: String
) {
    companion object {
        @JvmStatic
        val EMPTY = Country("","")
    }
}

data class CountryResponse(
    val currencies : List<CurrencyResponse>,
    val flag: String
)

data class CurrencyResponse(
    val name: String
)

