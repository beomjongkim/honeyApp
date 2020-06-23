package com.dmonster.darling.honey.point.data

import com.google.gson.annotations.SerializedName

/*    마켓 상품구매, 선물    */
class MarketBuyData {

    @SerializedName("idx")
    var idx: String? = null

    @SerializedName("mb_id")
    var mbId: String? = null

    @SerializedName("mb_hp")
    var mbHp: String? = null

    @SerializedName("it_id")
    var itId: String? = null

    @SerializedName("it_coin")
    var itCoin: String? = null

    @SerializedName("receiver_id")
    var receiverId: String? = null

    @SerializedName("receiver_hp")
    var receiverHp: String? = null

    @SerializedName("it_qty")
    var itQty: String? = null

    @SerializedName("it_state")
    var itState: String? = null

    @SerializedName("it_info")
    var itInfo: String? = null

    @SerializedName("reg_date")
    var regDate: String? = null

    @SerializedName("end_date")
    var endDate: String? = null

}
