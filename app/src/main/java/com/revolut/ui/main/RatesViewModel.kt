package com.revolut.ui.main

import androidx.lifecycle.MutableLiveData
import com.revolut.RatesMap
import com.revolut.SchedulersProvider
import com.revolut.interactor.RatesInteractor
import com.revolut.model.Rates
import com.revolut.ui.BaseViewModel
import com.revolut.uiSubscribe
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val scheduler: SchedulersProvider,
    private val ratesInteractor: RatesInteractor
) : BaseViewModel() {

    val rates: MutableLiveData<RatesMap> by lazy {
        MutableLiveData<RatesMap>()
    }

    init {
        getRates()
    }

    private fun getRates() {
        addReaction(
            Observable
                .interval(0,1, TimeUnit.SECONDS, scheduler.background)
                .flatMap { ratesInteractor.fetchRates() }
                .uiSubscribe(scheduler)
                .subscribe(
                    { rates.value = it },
                    { handleError(it) }
                )
        )
    }

    private fun handleError(error: Throwable) {
        error.printStackTrace()
    }

}
