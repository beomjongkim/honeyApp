package com.dmonster.darling.honey.main.data

import com.google.gson.annotations.SerializedName

/*    회원목록    */
class MainListData {

    @SerializedName("mb_no")
    var mbNo: String? = null

    @SerializedName("mb_id")
    var mbId: String? = null

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

    @SerializedName("mb_lat")
    var mb_lat: String? = null

    @SerializedName("mb_lng")
    var mb_lng: String? = null

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

    @SerializedName("mb_profile_color")
    var mbProfileColor: String? = null

    @SerializedName("mb_img")
    var mbImg: String? = null

    @SerializedName("mb_img_thumb")
    var mbImgThumb: String? = null

    @SerializedName("mb_img_cnt")
    var mbImgCnt: String? = null

    @SerializedName("mb_animal")
    var mbAnimal: String? = null

    @SerializedName("item_use_profile")
    var itemUseProfile: String? = null

}
