package com.revolut

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.gson.internal.LinkedTreeMap
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

typealias RatesMap = LinkedTreeMap<String, Float>

fun <T> Observable<T>.uiSubscribe(schedulers: SchedulersProvider): Observable<T> {
    return subscribeOn(schedulers.background).observeOn(schedulers.main)
}

fun <T> Observable<T>.backgroundSubscribe(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
}

fun <T> Single<T>.singleUiSubscribe(schedulers: SchedulersProvider): Single<T> {
    return subscribeOn(schedulers.background).observeOn(schedulers.main)
}


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}