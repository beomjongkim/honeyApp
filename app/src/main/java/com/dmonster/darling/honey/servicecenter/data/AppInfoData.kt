package com.dmonster.darling.honey.servicecenter.data

import android.text.TextUtils
import com.google.gson.annotations.SerializedName

/*    앱 정보    */
class AppInfoData {

    @SerializedName("method")
    var method: String? = null

    @SerializedName("result")
    var result: String? = null

    @SerializedName("count")
    var count: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("app_ver")
    var appVer: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("ceo")
    var ceo: String? = null

    @SerializedName("address")
    var address: String? = null

    @SerializedName("call_no")
    var callNo: String? = null

    @SerializedName("biz_no")
    var bizNo: String? = null

    @SerializedName("report_no")
    var reportNo: String? = null

    val isSuccess: Boolean
        get() = !TextUtils.isEmpty(result) && result == "1"

}
