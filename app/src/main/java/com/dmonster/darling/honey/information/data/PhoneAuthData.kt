package com.dmonster.darling.honey.information.data

import com.google.gson.annotations.SerializedName

/*    휴대폰번호, 인증번호    */
class PhoneAuthData {

    @SerializedName("ret_str")
    var retStr: String? = null

    @SerializedName("certno")
    var certno: String? = null

    @SerializedName("msg")
    var msg: String? = null

}
