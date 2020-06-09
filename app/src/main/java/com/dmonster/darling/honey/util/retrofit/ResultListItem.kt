package com.dmonster.darling.honey.util.retrofit

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class ResultListItem<T> : BaseItem() {

    @SerializedName("item")
    val items: ArrayList<T>? = null

}
