package com.revolut.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.global.test.globaltest.network.WebClient
import com.revolut.R
import com.revolut.RatesMap
import com.revolut.SchedulersProvider
import com.revolut.interactor.RatesInteractorImpl
import com.revolut.model.Rate
import com.revolut.model.getCurrency
import kotlinx.android.synthetic.main.main_fragment.*

class RatesFragment : Fragment() {

    companion object {
        fun newInstance() = RatesFragment()
    }

    private lateinit var viewModel: RatesViewModel
    private val adapter = RatesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(RatesViewModel::class.java)
        viewModel = RatesViewModel(SchedulersProvider.Impl(), RatesInteractorImpl(WebClient().dataService()))
        setUpList()

    }

    private fun setUpList() {
        rates_list.adapter = adapter

        adapter.listListener = object : RateListListener {
            override fun onValueChanged(value: String) {
                    viewModel.setNewValue(value)
            }

            override fun onItemSelected(position: Int) {
                viewModel.selectItem(position)
            }
        }

        val ratesObserver = Observer<List<Rate>> { rates ->
            if (!rates_list.isComputingLayout) {
                adapter.setData(rates)
            }
        }

        viewModel.rates.observe(viewLifecycleOwner, ratesObserver)
    }

}
