package com.dmonster.darling.honey.item.data

import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerViewItemVM
import com.google.gson.annotations.SerializedName

/*    아이템    */
class PointLogData : RecyclerViewItemVM("") {

    @SerializedName("it_point")
    var it_point: String? = null

    @SerializedName("lib_on_created")
    var date: String? = null

    @SerializedName("it_description")
    var it_name: String? = null

    @SerializedName("it_name")
    var it_name_eng: String? = null


}
