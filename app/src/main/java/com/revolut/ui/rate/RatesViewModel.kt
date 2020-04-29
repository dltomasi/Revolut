package com.revolut.ui.rate

import androidx.lifecycle.MutableLiveData
import com.revolut.backgroundSubscribe
import com.revolut.country.interactor.CountryInteractor
import com.revolut.rate.interactor.RatesInteractor
import com.revolut.rate.model.Rate
import com.revolut.rx.SchedulersProvider
import com.revolut.ui.BaseViewModel
import com.revolut.uiSubscribe
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class RatesViewModel constructor(
    private val scheduler: SchedulersProvider,
    private val ratesInteractor: RatesInteractor,
    private val countryInteractor: CountryInteractor
) : BaseViewModel() {

    private var originalRates = listOf<Rate>()
    val rates = MutableLiveData<List<Rate>>()
    val error = MutableLiveData<String>()

    private var first: Rate =
        START_CURRENCY

    init {
        getRates()
    }

    private fun getRates() {
        if (first.country == null) {
            addReaction(
                countryInteractor.getCountry(first.currency)
                    .backgroundSubscribe(scheduler)
                    .subscribe {
                        first.country = it
                    }
            )
        }
        addReaction(
            Observable
                .interval(0, TIME_INTERVAL, TimeUnit.SECONDS, scheduler.background)
                .flatMap {
                    ratesInteractor.fetchRates(first.currency)
                        .doOnNext { originalRates = it }
                        .flatMapIterable { it }
                        .flatMap { rate ->
                            if (rate.country == null) {
                                countryInteractor.getCountry(rate.currency)
                                    .map {
                                        rate.country = it
                                        rate
                                    }
                            } else {
                                Observable.just(rate)
                            }
                        }
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
        val newList = originalRates.map { Rate(it.currency, it.rate) }
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
        const val TIME_INTERVAL = 5L
        val START_CURRENCY = Rate("EUR", 1.0)
    }

}

