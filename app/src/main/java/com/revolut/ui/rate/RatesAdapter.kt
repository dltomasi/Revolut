package com.revolut.ui.rate

import android.net.Uri
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.revolut.R
import com.revolut.afterTextChanged
import com.revolut.rate.model.Rate
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
        val first = if (items.isEmpty()) Rate(
            "",
            0.0
        ) else items[0]
        items = rates.toMutableList()
        if (first.currency == rates[0].currency)
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
                rate.text = item.currency
                value.setText(item.rateText())
                setOnClickListener {
                    onClick(position)
                }
                value.isEnabled = position == 0
                value.removeTextChangedListener(listenerText)
                if (position == 0) {
                    listenerText = value.afterTextChanged { fieldValue ->
                        // avoid error when recycling view
                        if (rate.text == item.currency) {
                            clickListener.onValueChanged(fieldValue)
                        }
                    }
                }
                item.country?.let { country ->
                    name.text = country.name
                    GlideToVectorYou
                        .init()
                        .with(this.context)
                        .load(Uri.parse(country.flag), flag)

                } ?: run {
                    flag.setImageResource(android.R.drawable.ic_menu_upload)
                    name.text = "empty"
                }

            }
        }
    }
}


