package com.dmonster.darling.honey.point.data

import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerViewItemVM
import com.google.gson.annotations.SerializedName

/*    아이템    */
class PointLogData : RecyclerViewItemVM("") {

    @SerializedName("point")
    var point: String? = null

    @SerializedName("on_created")
    var date: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("it_name")
    var it_name_eng: String? = null

    @SerializedName("useOrCharge")
    var useOrCharge: Int? = null


    var date_split : String? = null
    var time_split : String? = null
}
