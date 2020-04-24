package com.revolut.ui.main

interface RateListListener {
    fun onValueChanged(value: String)
    fun onItemSelected(position: Int)
}
