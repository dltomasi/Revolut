package com.revolut.ui.rate

interface RateListListener {
    fun onValueChanged(value: String)
    fun onItemSelected(position: Int)
}
