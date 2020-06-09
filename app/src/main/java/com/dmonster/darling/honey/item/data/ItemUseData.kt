package com.dmonster.darling.honey.item.data

import com.google.gson.annotations.SerializedName

/*    아이템 사용    */
class ItemUseData {

    @SerializedName("idx")
    var idx: String? = null

    @SerializedName("mb_id")
    var mbId: String? = null

    @SerializedName("od_name")
    var odName: String? = null

    @SerializedName("od_tel")
    var odTel: String? = null

    @SerializedName("od_coin")
    var odCoin: String? = null

    @SerializedName("reg_date")
    var regDate: String? = null

    @SerializedName("od_info")
    var odInfo: String? = null

    @SerializedName("it_id")
    var itId: String? = null

    @SerializedName("it_name")
    var itName: String? = null

}
