package com.revolut.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.revolut.R
import com.revolut.RatesMap
import com.revolut.model.Rate
import com.revolut.model.getCurrency
import com.revolut.model.getRate
import kotlinx.android.synthetic.main.rate_item.view.*

class RatesAdapter : RecyclerView.Adapter<RateViewHolder>() {

    private var items = mutableListOf<Rate>()
    private var first = Rate("", 0F)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder(parent) {
            items.apply {
                first = removeAt(it)
                add(0, first)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(position, items[position])
    }

    fun setData(rates: RatesMap) {
        var item: Rate? = null
        rates[first.getCurrency()]?.let {
            item = Pair(first.getCurrency(), it)
            rates.remove(first.getCurrency())
        }
        items = rates.toList().toMutableList()
        item?.let {  items.add(0, it) }
        notifyDataSetChanged()
    }
}

class RateViewHolder(parent: ViewGroup, val onClick: (position: Int) -> Unit) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.rate_item, parent, false)
    ) {

    fun bind(position: Int, item: Rate) {
        itemView.apply {
            rate.text = item.first
            value.text = item.getRate()
            setOnClickListener {
                onClick(position)
            }
        }
    }
}
