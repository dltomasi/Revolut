package com.revolut.ui.rate

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.jakewharton.rxbinding2.widget.RxTextView
import com.revolut.R
import com.revolut.country.model.Country
import com.revolut.rate.model.Rate
import com.revolut.rate.model.copy
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.rate_item.view.*

class RatesAdapter : RecyclerView.Adapter<RatesAdapter.RateViewHolder>() {

    private var items = mutableListOf<Rate>()
    lateinit var clickListener: RateListListener
    var shouldUpdateFirst = false

    var textChangeObservable: PublishSubject<String> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder(parent) {
            if (items.size > 1) {
                items.add(0, items.removeAt(it))
            }
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
        val first = if (items.isEmpty()) Rate.EMPTY else items[0]
        items = rates.toMutableList().copy()
        if (first.country == rates[0].country)
        // to avoid changing focus on edit text
            notifyItemRangeChanged(1, items.size - 1)
        else notifyDataSetChanged()
    }

    inner class RateViewHolder(parent: ViewGroup, val onClick: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rate_item, parent, false)
        ) {

        fun bind(position: Int, item: Rate) {
            itemView.apply {
                rate.text = item.currency
                value.setText(item.rateText())
                setOnClickListener {
                    onClick(position)
                }
                value.isEnabled = position == 0
                if (position == 0) {
                    RxTextView.textChanges(value).skipInitialValue()
                        // avoid error when recycling view
                        .filter { rate.text == item.currency }
                        .map { it.toString() }
                        .subscribe(textChangeObservable)
                }

                item.country?.let { country ->
                    name.text = country.name
                    GlideToVectorYou
                        .init()
                        .with(this.context)
                        .load(Uri.parse(country.flag), flag)
                } ?: run {
                    name.text = ""
                }
            }
        }
    }
}


