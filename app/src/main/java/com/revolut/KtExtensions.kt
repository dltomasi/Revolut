package com.revolut

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.gson.internal.LinkedTreeMap
import com.revolut.rx.SchedulersProvider
import io.reactivex.Observable

typealias RatesMap = LinkedTreeMap<String, Double>

fun <T> Observable<T>.uiSubscribe(schedulers: SchedulersProvider): Observable<T> {
    return subscribeOn(schedulers.background).observeOn(schedulers.main)
}

fun <T> Observable<T>.backgroundSubscribe(schedulers: SchedulersProvider): Observable<T> {
    return subscribeOn(schedulers.background).observeOn(schedulers.background)
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) : TextWatcher {
    val watcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    }
    this.addTextChangedListener(watcher)
    return watcher
}