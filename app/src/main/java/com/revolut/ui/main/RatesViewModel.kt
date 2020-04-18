package com.revolut.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.revolut.SchedulersProvider
import com.revolut.interactor.RatesInteractor
import com.revolut.model.Rates
import com.revolut.ui.BaseViewModel
import com.revolut.uiSubscribe
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val scheduler: SchedulersProvider,
    private val ratesInteractor: RatesInteractor
) : BaseViewModel() {

    val rates: MutableLiveData<Rates> by lazy {
        MutableLiveData<Rates>()
    }

    init {
        getRates()
    }

    private fun getRates() {
        addReaction(
            ratesInteractor.fetchRates()
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
