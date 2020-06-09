package com.dmonster.darling.honey.util.retrofit

import android.text.TextUtils
import com.google.gson.annotations.SerializedName

open class BaseItem {

    @SerializedName("method")
    var method: String? = null

    @SerializedName("result")
    var result: String? = null

    @SerializedName("count")
    var count: String? = null

    @SerializedName("message")
    var message: String? = null

    val isSuccess: Boolean
        get() = !TextUtils.isEmpty(result) && result == "1"

}
