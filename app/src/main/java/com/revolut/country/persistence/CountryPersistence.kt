package com.revolut.country.persistence

class CountryPersistence {

    private val countries = hashMapOf<String, String>()

    fun get(code: String): String? = countries[code]

    fun set(code: String, flag: String) {
        countries[code] = flag
    }
}