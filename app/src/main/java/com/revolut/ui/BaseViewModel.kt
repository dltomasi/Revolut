package com.revolut.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    val progress = MutableLiveData<Boolean>()

    fun showProgress() {
        progress.value = true
    }

    fun hideProgress() {
        progress.value = false
    }

    protected fun addReaction(reaction: Disposable) {
        disposable.add(reaction)
    }

    fun onStop() {
        // clear all the subscriptions
        disposable.clear()
    }
}
