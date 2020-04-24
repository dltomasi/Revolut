package com.revolut.ui.main

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolut.R
import com.revolut.afterTextChanged
import com.revolut.model.Rate
import com.revolut.model.getCurrency
import com.revolut.model.getRate
import kotlinx.android.synthetic.main.rate_item.view.*

class RatesAdapter : RecyclerView.Adapter<RatesAdapter.RateViewHolder>() {

    private var items = mutableListOf<Rate>()
    lateinit var clickListener: RateListListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder(parent) {
            items.add(0, items.removeAt(it))
            notifyItemMoved(it, 0)
            notifyItemChanged(0)
            clickListener.onItemSelected(it)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(position, items[position])
    }

    fun setData(rates: List<Rate>) {
        val first = if (items.isEmpty()) Rate("", 0F) else items[0]
        items = rates.toMutableList()
        if (first.getCurrency() == rates[0].getCurrency())
            // to avoid changing focus on edit text
            notifyItemRangeChanged(1, items.size - 1)
        else notifyDataSetChanged()
    }

    inner class RateViewHolder(parent: ViewGroup, val onClick: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rate_item, parent, false)
        ) {

        private var listenerText: TextWatcher? = null
        fun bind(position: Int, item: Rate) {
            itemView.apply {
                rate.text = item.getCurrency()
                value.setText(item.getRate())
                setOnClickListener {
                    onClick(position)
                }
                value.isEnabled = position == 0
                value.removeTextChangedListener(listenerText)
                if (position == 0) {
                    listenerText = value.afterTextChanged { fieldValue ->
                        // avoid error when recycling view
                        if (rate.text == item.getCurrency()) {
                            clickListener.onValueChanged(fieldValue)
                        }
                    }
                }
            }
        }
    }
}


