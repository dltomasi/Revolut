package com.revolut.ui.main

import androidx.lifecycle.MutableLiveData
import com.revolut.rx.SchedulersProvider
import com.revolut.interactor.RatesInteractor
import com.revolut.model.Rate
import com.revolut.model.currency
import com.revolut.model.rateValue
import com.revolut.ui.BaseViewModel
import com.revolut.rx.uiSubscribe
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val scheduler: SchedulersProvider,
    private val ratesInteractor: RatesInteractor
) : BaseViewModel() {

    var originalRates = mutableListOf<Rate>()
    val rates = MutableLiveData<List<Rate>>()
    val error = MutableLiveData<String>()

    private var first: Rate = START_CURRENCY

    init {
        getRates()
    }

    private fun getRates() {
        addReaction(
            Observable
                .interval(0, TIME_INTERVAL, TimeUnit.SECONDS, scheduler.background)
                .flatMap {
                    ratesInteractor.fetchRates(first.currency())
                        .map { it.toList() }
                        .doOnNext{originalRates = it.toMutableList()}
                        .flatMapIterable { it }
                        .map { it.copy(second = it.rateValue(first)) }
                        .toList().toObservable()
                }
                .uiSubscribe(scheduler)
                .subscribe(
                    {
                        it.add(0, first)
                        rates.value = it
                    },
                    { handleError(it) }
                )
        )
    }

    private fun handleError(e: Throwable) {
        e.printStackTrace()
        error.value = e.localizedMessage
    }

    fun setNewValue(value: String) {
        first = if (value.isEmpty()) {
            first.copy(second = 0.0)
        } else {
            first.copy(second = value.toDouble())
        }
        val newList =  originalRates.map { it.copy(second = it.rateValue(first)) }.toMutableList()
        newList.add(0, first)
        rates.value = newList
    }

    fun selectItem(position: Int) {
        first = rates.value!![position]
        val newList =
            rates.value!!
                .filter { it.currency() != first.currency() }
                .toMutableList()
        newList.add(0, first)
        rates.value = newList
    }

    companion object {
        const val TIME_INTERVAL = 3L
        val START_CURRENCY = Pair("EUR", 1.0)
    }

}

