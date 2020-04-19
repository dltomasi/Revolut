package com.revolut.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.LinkedTreeMap
import com.revolut.R
import com.revolut.RatesMap
import com.revolut.model.Rates
import kotlinx.android.synthetic.main.rate_item.view.*

class RatesAdapter : RecyclerView.Adapter<RateViewHolder>() {

    private var items = RatesMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder(parent, {})
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(position, items.toList()[position])
    }

    fun setData(rates: RatesMap) {
        items = rates
        notifyDataSetChanged()
    }
}

class RateViewHolder(parent: ViewGroup, val onClick: (position: Int) -> Unit) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.rate_item, parent, false)
    ) {

    fun bind(position: Int, item: Pair<String, Float>) {
        itemView.apply {
            rate.text = item.first
            value.text = item.second.toString()
            setOnClickListener {
                onClick(position)
            }
        }
    }
}
