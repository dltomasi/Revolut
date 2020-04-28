package com.revolut.country.model


data class Country(
    val name: String,
    val flag: String
)

data class CountryResponse(
    val currencies : List<CurrencyResponse>,
    val flag: String
)

data class CurrencyResponse(
    val name: String
)

/*
"currencies":[
         {
            "code":"BRL",
            "name":"Brazilian real",
            "symbol":"R$"
         }
      ],
 */

