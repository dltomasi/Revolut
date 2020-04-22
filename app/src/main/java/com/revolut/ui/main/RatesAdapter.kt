package com.revolut.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.revolut.R
import com.revolut.RatesMap
import com.revolut.afterTextChanged
import com.revolut.model.Rate
import com.revolut.model.getCurrency
import com.revolut.model.getRate
import kotlinx.android.synthetic.main.rate_item.view.*

class RatesAdapter : RecyclerView.Adapter<RatesAdapter.RateViewHolder>() {

    private var items = emptyList<Rate>()
    lateinit var listListener: RateListListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder(parent) {
            items.apply {
                // send to top
//                selected.value = removeAt(it)
//                add(0, selected.value!!)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(position, items[position])
    }



    fun setData(rates: List<Rate>) {
        items = rates
        //notifyItemRangeChanged(1, items.size - 1)
        notifyDataSetChanged()
    }

    inner class RateViewHolder(parent: ViewGroup, val onClick: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rate_item, parent, false)
        ) {

        fun bind(position: Int, item: Rate) {
            itemView.apply {
                rate.text = item.getCurrency()
                value.setText(item.getRate())
                setOnClickListener {
                    onClick(position)
                }
                if (position == 0) {
                    value.afterTextChanged { fieldValue ->
                        // logic
                        fieldValue.toFloat().apply {
                           // update values
                            notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
}


