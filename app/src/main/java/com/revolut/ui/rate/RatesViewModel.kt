package com.revolut.ui.rate

import androidx.lifecycle.MutableLiveData
import com.revolut.backgroundSubscribe
import com.revolut.country.interactor.CountryInteractor
import com.revolut.rate.interactor.RatesInteractor
import com.revolut.rate.model.Rate
import com.revolut.rate.model.copy
import com.revolut.rx.SchedulersProvider
import com.revolut.ui.BaseViewModel
import com.revolut.uiSubscribe
import io.reactivex.Observable
import java.lang.Exception
import java.util.concurrent.TimeUnit

class RatesViewModel constructor(
    private val scheduler: SchedulersProvider,
    private val ratesInteractor: RatesInteractor,
    private val countryInteractor: CountryInteractor
) : BaseViewModel() {

    var textChangeObservable: Observable<String> = Observable.empty()
    private var originalRates = mutableListOf<Rate>()
    val rates = MutableLiveData<List<Rate>>()
    val error = MutableLiveData<Boolean>()

    private var first: Rate = START_CURRENCY

    private fun startLoading() {
        if (originalRates.isEmpty()) {
            showProgress()
        }
    }

    private fun setUpReactions() {
        getFlagForFirstItem()
        updateRatesOverTime()
        textChangeListener()
    }

    private fun updateRatesOverTime() {
        addReaction(
            Observable
                .interval(0, TIME_INTERVAL, TimeUnit.SECONDS, scheduler.background)
                .observeOn(scheduler.main)
                .doOnNext { startLoading() }
                .observeOn(scheduler.background)
                .flatMapSingle {
                    ratesInteractor.fetchRates(first.currency)
                        .map {
                            originalRates = it.copy()
                            it
                        }
                        .flatMapIterable { it }
                        .map {
                            it.rate = it.rateValue(first)
                            it
                        }
                        .toList()
                }
                .observeOn(scheduler.main)
                .doOnNext { hideProgress() }
                .subscribeOn(scheduler.background)
                .subscribe(::onSuccess, ::handleError)
        )
    }

    private fun getFlagForFirstItem() {
        if (first.country == null) {
            addReaction(
                countryInteractor.getCountry(first.currency).toObservable()
                    .backgroundSubscribe(scheduler)
                    .subscribe(
                        { first.country = it },
                        { it.printStackTrace() }
                    )
            )
        }
    }

    private fun textChangeListener() {
        addReaction(
            textChangeObservable
                .uiSubscribe(scheduler)
                .subscribe {
                    setNewValue(it)
                }
        )
    }

    private fun onSuccess(data: MutableList<Rate>) {
        data.add(0, first)
        rates.value = data
    }

    private fun handleError(e: Throwable) {
        e.printStackTrace()
        hideProgress()
        error.value = originalRates.isEmpty()
    }

    private fun setNewValue(value: String) {
        try {
            first.rate = value.toDouble()
        } catch (e: Exception) {
            first.rate = 0.0
        }
        val newList = originalRates.copy()
            .map {
                it.rate = it.rateValue(first)
                it
            }.toMutableList()
        newList.add(0, first)
        rates.value = newList
    }

    fun selectItem(position: Int) {
        first = rates.value!![position]
        originalRates.clear()
        showProgress()
    }

    fun tryAgain() {
        error.value = false
        setUpReactions()
    }

    fun onStart() {
        setUpReactions()
    }

    companion object {
        const val TIME_INTERVAL = 1L
        val START_CURRENCY = Rate("EUR", 1.0)
    }

}

