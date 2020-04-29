package com.revolut.country.background

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.revolut.country.interactor.CountryWebInteractor
import io.reactivex.Single
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class CountryWorkManager(
    context: Context,
    params: WorkerParameters
) : RxWorker(context, params), KoinComponent {

    private val countryWebInteractor: CountryWebInteractor by inject()

    override fun createWork(): Single<Result> {
        val base = inputData.getString(BASE_KEY)!!
        return countryWebInteractor.fetchCountry(base)
            .map { Result.success() }
    }

    companion object {
        const val BASE_KEY = "base"
    }
}