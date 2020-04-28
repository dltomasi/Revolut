package com.revolut.injection

import android.content.Context
import android.content.SharedPreferences
import com.revolut.country.interactor.CountryInteractor
import com.revolut.country.interactor.CountryInteractorImpl
import com.revolut.country.network.CountryWebClient
import com.revolut.country.persistence.CountryPersistence
import com.revolut.rate.interactor.RatesInteractor
import com.revolut.rate.interactor.RatesInteractorImpl
import com.revolut.rate.network.RateWebClient
import com.revolut.rx.SchedulersProvider
import com.revolut.ui.rate.RatesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val rateModule = module {
    single { RateWebClient().rateService() }
    factory<RatesInteractor> { RatesInteractorImpl(get()) }
    single<SchedulersProvider> { SchedulersProvider.Impl() }
    viewModel { RatesViewModel(get(), get(), get()) }
}

val countryModule = module {
    single { CountryWebClient().countryService() }
    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            "SharedPreferences",
            Context.MODE_PRIVATE
        )
    }
    single { CountryPersistence(get()) }
    factory<CountryInteractor> { CountryInteractorImpl(get(), get()) }
}