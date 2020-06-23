package com.dmonster.darling.honey.point.data

import com.google.gson.annotations.SerializedName

/*    마켓 상품목록    */
class MarketListData {

    @SerializedName("idx")
    var idx: String? = null

    @SerializedName("it_order")
    var itOrder: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("price")
    var price: String? = null

    @SerializedName("thumb")
    var thumb: String? = null

}
