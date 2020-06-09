package com.dmonster.darling.honey.information.data

import com.google.gson.annotations.SerializedName

/*    기본정보    */
class MyInfoData {

    @SerializedName("mb_no")
    var mbNo: String? = null

    @SerializedName("mb_id")
    var mbId: String? = null

    @SerializedName("mb_nick")
    var mbNick: String? = null

    @SerializedName("mb_char")
    var mbChar: String? = null

    @SerializedName("mb_birth")
    var mbBirth: String? = null

    @SerializedName("mb_age")
    var mbAge: String? = null

    @SerializedName("mb_addr1")
    var mbAddr1: String? = null

    @SerializedName("mb_addr2")
    var mbAddr2: String? = null

    @SerializedName("mb_hp")
    var mbHp: String? = null

    @SerializedName("mb_nick_cnt")
    var mbNickCnt: String? = null

    @SerializedName("mb_birth_cnt")
    var mbBirthCnt: String? = null

    @SerializedName("mb_char_cnt")
    var mbCharCnt: String? = null

}
