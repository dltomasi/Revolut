package com.revolut

import android.app.Application
import com.revolut.injection.countryModule
import com.revolut.injection.rateModule
import org.koin.android.ext.android.startKoin
import org.koin.core.Koin
import org.koin.log.EmptyLogger

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        val moduleList = listOf(rateModule, countryModule)
        startKoin(this, moduleList)
        Koin.logger = EmptyLogger()
    }
}