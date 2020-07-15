package com.dmonster.darling.honey.login.data

import com.google.gson.annotations.SerializedName

/*    로그인    */
class LoginData {

    @SerializedName("mb_no")
    var mbNo: String? = null

    @SerializedName("mb_sn")
    var mbSn: String? = null

    @SerializedName("mb_id")
    var mbId: String? = null

    @SerializedName("mb_nick")
    var mbNick: String? = null

    @SerializedName("mb_password")
    var mbPassword: String? = null

    @SerializedName("mb_sex")
    var mbSex: String? = null

    @SerializedName("mb_sleep")
    var mbSleep: String? = null

    @SerializedName("mb_profile_state")
    var mbProfileState: String? = null

}
