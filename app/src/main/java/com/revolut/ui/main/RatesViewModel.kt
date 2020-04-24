package com.revolut.ui.main

import androidx.lifecycle.MutableLiveData
import com.revolut.SchedulersProvider
import com.revolut.interactor.RatesInteractor
import com.revolut.model.Rate
import com.revolut.model.getCurrency
import com.revolut.model.getRateValue
import com.revolut.ui.BaseViewModel
import com.revolut.uiSubscribe
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val scheduler: SchedulersProvider,
    private val ratesInteractor: RatesInteractor
) : BaseViewModel() {

//    val rates: MutableLiveData<List<Rate>> by lazy {
//        MutableLiveData<List<Rate>>()
//    }

    val rates = MutableLiveData<List<Rate>>()

    var first: Rate = START_CURRENCY

    init {
        getRates()
    }

    private fun getRates() {
        addReaction(
            Observable
                .interval(0, TIME_INTERVAL, TimeUnit.SECONDS, scheduler.background)
                .flatMap {
                    ratesInteractor.fetchRates(first.getCurrency())
                        .map { it.toList() }
                        .flatMapIterable { it }
                        .map { it.copy(second = it.getRateValue() * first.getRateValue()) }
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

    private fun handleError(error: Throwable) {
        error.printStackTrace()
    }

    fun setNewValue(value: String) {
        first = if (value.isEmpty()) {
            first.copy(second = 0F)
        } else {
            first.copy(second = value.toFloat())
        }
        rates.value =
            rates.value!!.map { it.copy(second = it.getRateValue() * first.getRateValue()) }
    }

    fun selectItem(position: Int) {
        first = rates.value!![position]
        val newList =
            rates.value!!
                .filter { it.getCurrency() != first.getCurrency() }
                .toMutableList()
        newList.add(0, first)
        rates.value = newList
    }

    companion object {
        const val TIME_INTERVAL = 1L
        val START_CURRENCY = Pair("EUR", 1F)
    }

}

