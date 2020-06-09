package com.dmonster.darling.honey.item.data

import com.google.gson.annotations.SerializedName

/*    아이템    */
class ItemData {

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

    @SerializedName("it_term")
    var itTerm: String? = null

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

    @SerializedName("it_val1")
    var itVal1: String? = null

    @SerializedName("it_name")
    var itName: String? = null

    @SerializedName("it_term_hour")
    var itTermHour: String? = null

    @SerializedName("it_term_date")
    var itTermDate: String? = null

    @SerializedName("it_limit_cnt")
    var itLimitCnt: String? = null

}
