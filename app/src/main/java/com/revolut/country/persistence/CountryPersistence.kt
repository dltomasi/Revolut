package com.revolut.country.persistence

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.revolut.country.model.Country
import java.lang.reflect.Type

class CountryPersistence(private val storage: SharedPreferences) {

    private val gson = Gson()
    private var countries = mutableMapOf<String, Country>()

    init {
        val storedHashMapString: String = storage.getString(MAP_KEY, "")!!
        val type: Type = object : TypeToken<HashMap<String?, Country?>?>() {}.type
        if (!storedHashMapString.isNullOrEmpty())
            countries = gson.fromJson(storedHashMapString, type)
    }

    fun get(code: String): Country? = countries[code]

    fun set(code: String, country: Country) {
        countries[code] = country
        storage.edit().putString(MAP_KEY, gson.toJson(countries)).apply()
    }

    companion object {
        const val MAP_KEY = "hashString"
    }
}