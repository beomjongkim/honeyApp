package com.dmonster.darling.honey.login.data

import com.google.gson.annotations.SerializedName

/*    아이디/비밀번호 찾기    */
class FindIDPWData {

    @SerializedName("ret_str")
    var retStr: String? = null

    @SerializedName("mb_id")
    var mbId: String? = null

    @SerializedName("change_pw")
    var changePw: String? = null

}
