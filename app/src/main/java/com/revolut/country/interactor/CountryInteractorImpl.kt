package com.revolut.country.interactor


import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.revolut.country.background.CountryWorkManager
import com.revolut.country.background.CountryWorkManager.Companion.BASE_KEY
import com.revolut.country.model.Country
import com.revolut.country.persistence.CountryPersistence
import io.reactivex.Single

class CountryInteractorImpl(
    private val countryPersistence: CountryPersistence,
    private val workManager: WorkManager
) : CountryInteractor {

    private val workers = hashMapOf<String, Boolean>()

    override fun getCountry(base: String): Single<Country> =
        countryPersistence.get(base)?.let {
            Single.just(it)
        } ?: run {
            createWorker(base)
            Single.just(Country.EMPTY)
        }

    private fun createWorker(base: String) {
        if (workers[base] == null) {
            workers[base] = true
            val input: Data = Data.Builder()
                .putString(BASE_KEY, base)
                .build()
            workManager.enqueue(
                OneTimeWorkRequest.Builder(CountryWorkManager::class.java)
                    .addTag(base)
                    .setInputData(input)
                    .build()
            )
        }
    }

}