package com.revolut.ui.rate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.revolut.R
import com.revolut.rate.model.Rate
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class RatesFragment : Fragment() {

    companion object {
        fun newInstance() = RatesFragment()
    }

    private val viewModel: RatesViewModel by viewModel()
    private val adapter = RatesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpList()
        errorObserver()
        progressObserver()
        try_again.setOnClickListener {
            viewModel.tryAgain()
        }
    }


    override fun onStart() {
        super.onStart()
        viewModel.onStart()
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
        val observer = Observer<Boolean> { error ->
            error_view.visibility = if (error) VISIBLE else GONE
        }

        viewModel.error.observe(viewLifecycleOwner, observer)
    }

    private fun progressObserver() {
        val observer = Observer<Boolean> { isLoading ->
            progress_bar.visibility = if (isLoading) VISIBLE else GONE
        }

        viewModel.progress.observe(viewLifecycleOwner, observer)
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }
}
