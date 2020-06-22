package com.dmonster.darling.honey.point.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.point.data.MarketListData
import de.hdodenhof.circleimageview.CircleImageView

class MarketAdapter(): androidx.recyclerview.widget.RecyclerView.Adapter<MarketAdapter.ViewHolder>() {

    var data = arrayListOf<MarketListData>()
    var itemClick: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_market, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var productImage: CircleImageView = view.findViewById(R.id.civ_item_market_image)
        var productText: TextView = view.findViewById(R.id.tv_item_market_product)
        var priceText: TextView = view.findViewById(R.id.tv_item_market_price)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.tag = position
        holder.view.setOnClickListener(itemClick)
        val items = data[position]

        Glide.with(holder.view.context).load(items.thumb).into(holder.productImage)
        holder.productText.text = items.name
        holder.priceText.text = items.price
    }

}
