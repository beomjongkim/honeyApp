package com.dmonster.darling.honey.util.retrofit

import com.google.gson.annotations.SerializedName

class ResultItem<T> : BaseItem() {

    @SerializedName("item")
    val item: T? = null

}
