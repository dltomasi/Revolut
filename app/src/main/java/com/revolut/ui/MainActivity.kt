package com.revolut.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.revolut.R
import com.revolut.ui.main.RatesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, RatesFragment.newInstance())
                    .commitNow()
        }
    }
}
