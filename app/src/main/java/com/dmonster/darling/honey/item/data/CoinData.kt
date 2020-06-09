package com.dmonster.darling.honey.item.data

import com.google.gson.annotations.SerializedName

/*    현재 보유코인    */
class CoinData {

    @SerializedName("cnt_coin")
    var cntCoin: String? = null

    @SerializedName("ev_coin")
    var evCoin: String? = null

}
