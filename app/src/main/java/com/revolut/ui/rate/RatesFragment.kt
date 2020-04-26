package com.revolut.ui.rate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.revolut.rate.network.RateWebClient
import com.revolut.R
import com.revolut.rx.SchedulersProvider
import com.revolut.rate.interactor.RatesInteractorImpl
import com.revolut.rate.model.Rate
import kotlinx.android.synthetic.main.main_fragment.*

class RatesFragment : Fragment() {

    companion object {
        fun newInstance() = RatesFragment()
    }

    private lateinit var viewModel: RatesViewModel
    private val adapter = RatesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(RatesViewModel::class.java)
        viewModel = RatesViewModel(
            SchedulersProvider.Impl(),
            RatesInteractorImpl(RateWebClient().dataService())
        )
        setUpList()
        errorObserver()
    }

    private fun setUpList() {
        rates_list.adapter = adapter

        adapter.clickListener = object : RateListListener {
            override fun onValueChanged(value: String) {
                viewModel.setNewValue(value)
            }

            override fun onItemSelected(position: Int) {
                rates_list.scrollToPosition(0)
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

    private fun errorObserver() {
        val ratesObserver = Observer<String> { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        viewModel.error.observe(viewLifecycleOwner, ratesObserver)
    }

}