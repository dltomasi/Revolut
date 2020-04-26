package com.revolut.ui.rate

import androidx.lifecycle.MutableLiveData
import com.revolut.rx.SchedulersProvider
import com.revolut.rate.interactor.RatesInteractor
import com.revolut.rate.model.Rate
import com.revolut.ui.BaseViewModel
import com.revolut.uiSubscribe
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class RatesViewModel constructor(
    private val scheduler: SchedulersProvider,
    private val ratesInteractor: RatesInteractor
) : BaseViewModel() {

    var originalRates = mutableListOf<Rate>()
    val rates = MutableLiveData<List<Rate>>()
    val error = MutableLiveData<String>()

    private var first: Rate =
        START_CURRENCY

    init {
        getRates()
    }

    private fun getRates() {
        addReaction(
            Observable
                .interval(0,
                    TIME_INTERVAL, TimeUnit.SECONDS, scheduler.background)
                .flatMap {
                    ratesInteractor.fetchRates(first.currency)
                        .map { it.toList() }
                        .doOnNext{originalRates = it.toMutableList()}
                        .flatMapIterable { it }
                        .map {
                            it.rate = it.rateValue(first)
                            it
                        }
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
         if (value.isEmpty()) {
            first.rate = 0.0
        } else {
            first.rate = value.toDouble()
        }
        val newList =  originalRates
            .map {
                it.rate = it.rateValue(first)
                it
            }.toMutableList()
        newList.add(0, first)
        rates.value = newList
    }

    fun selectItem(position: Int) {
        first = rates.value!![position]
        val newList =
            rates.value!!
                .filter { it.currency != first.currency }
                .toMutableList()
        newList.add(0, first)
        rates.value = newList
    }

    companion object {
        const val TIME_INTERVAL = 3L
        val START_CURRENCY = Rate("EUR", 1.0)
    }

}
