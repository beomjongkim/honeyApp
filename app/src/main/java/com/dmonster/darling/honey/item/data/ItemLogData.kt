package com.dmonster.darling.honey.item.data

import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerViewItemVM
import com.google.gson.annotations.SerializedName

/*    아이템    */
class ItemLogData : RecyclerViewItemVM("") {

    @SerializedName("mb_nick")
    var mb_nick: String? = null

    @SerializedName("lib_on_created")
    var date: String? = null

    @SerializedName("it_description")
    var it_name: String? = null

    @SerializedName("it_name")
    var it_name_eng: String? = null


}
