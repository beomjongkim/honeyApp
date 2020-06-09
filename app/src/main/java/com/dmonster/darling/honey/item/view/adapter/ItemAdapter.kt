package com.dmonster.darling.honey.item.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.item.data.ItemUseData
import com.dmonster.darling.honey.util.AppKeyValue

class ItemAdapter(val type: String?): androidx.recyclerview.widget.RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var data = arrayListOf<ItemUseData>()
    var itemClick: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_item_admin, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var itemLayout: RelativeLayout = view.findViewById(R.id.rl_item_item_admin_layout)
        var itemImg: ImageView = view.findViewById(R.id.iv_item_item_admin_image)
        var titleText: TextView = view.findViewById(R.id.tv_item_item_admin_title)
        var timeText: TextView = view.findViewById(R.id.tv_item_item_admin_time)
        var contentText: TextView = view.findViewById(R.id.tv_item_item_admin_content)
        var coinText: TextView = view.findViewById(R.id.tv_item_item_admin_coin)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.setOnClickListener(itemClick)
        holder.view.tag = position
        val items = data[position]

        if(type == AppKeyValue.instance.itemTypeAdmin) {
            if(position > 2) {
                holder.itemLayout.visibility = View.GONE
            }
        }

        holder.view.context?.let {
            when(items.odInfo) {
                it.resources.getString(R.string.key_item_use01) -> {
                    when(items.itId) {
                        AppKeyValue.instance.itemIdTalk -> {
                            holder.itemImg.setImageResource(R.drawable.item_talk_img)
                        }

                        AppKeyValue.instance.itemIdProfile -> {
                            holder.itemImg.setImageResource(R.drawable.item_profile_img)
                        }

                        AppKeyValue.instance.itemIdGood -> {
                            holder.itemImg.setImageResource(R.drawable.item_good_img)
                        }

                        AppKeyValue.instance.itemIdLove -> {
                            holder.itemImg.setImageResource(R.drawable.item_love_img)
                        }

                        AppKeyValue.instance.itemIdRefresh -> {
                            holder.itemImg.setImageResource(R.drawable.item_refresh_img)
                        }
                    }
                }

                it.resources.getString(R.string.key_item_use02) -> {
                    holder.itemImg.setImageResource(R.drawable.item_buy_img)
                }

                it.resources.getString(R.string.key_item_use03),
                it.resources.getString(R.string.key_item_use04),
                it.resources.getString(R.string.key_item_use05) -> {
                    holder.itemImg.setImageResource(R.drawable.item_charge_img)
                }

                else -> {
                    holder.itemImg.setImageResource(R.drawable.item_charge_img)
                }
            }

            holder.titleText.text = items.itName
            holder.timeText.text = items.regDate
            holder.contentText.text = items.odInfo
            holder.coinText.text = String.format(it.resources.getString(R.string.item_now_coin), items.odCoin)
        }
    }

}
