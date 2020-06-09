package com.dmonster.darling.honey.block.data

import com.google.gson.annotations.SerializedName

/*    차단회원    */
class BlockData {

    @SerializedName("idx")
    var idx: String? = null

    @SerializedName("mb_id")
    var mbId: String? = null

    @SerializedName("mb_block")
    var mb_block: String? = null

    @SerializedName("reg_date")
    var reg_date: String? = null

    @SerializedName("read_date")
    var read_date: String? = null

    @SerializedName("mb_no")
    var mbNo: String? = null

    @SerializedName("mb_name")
    var mbName: String? = null

    @SerializedName("mb_name_chk")
    var mbNameChk: String? = null

    @SerializedName("mb_nick")
    var mbNick: String? = null

    @SerializedName("mb_sex")
    var mbSex: String? = null

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

    @SerializedName("mb_car")
    var mbCar: String? = null

    @SerializedName("mb_style")
    var mbStyle: String? = null

    @SerializedName("mb_char")
    var mbChar: String? = null

    @SerializedName("mb_profile")
    var mbProfile: String? = null

    @SerializedName("mb_img")
    var mbImg: String? = null

    @SerializedName("mb_img_thumb")
    var mbImgThumb: String? = null

    var isChecked: Boolean = false

}
