package com.dmonster.darling.honey.myactivity.data

import com.dmonster.darling.honey.profile.data.ProfilePictureData
import com.google.gson.annotations.SerializedName

/*    프로필 열람    */
class ProfileData {

    @SerializedName("idx")
    var idx: String? = null

    @SerializedName("mb_id")
    var mbId: String? = null

    @SerializedName("mb_visit")
    var mbVisit: String? = null

    @SerializedName("reg_date")
    var regDate: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("mb_name")
    var mbName: String? = null

    @SerializedName("mb_img")
    var mbImg: ProfilePictureData? = null

    @SerializedName("mb_img_thumb")
    var mbImgThumb: String? = null

    @SerializedName("mb_img_cnt")
    var mbImgCnt: String? = null

    @SerializedName("mb_no")
    var mbNo: String? = null

    @SerializedName("mb_nick")
    var mbNick: String? = null

    @SerializedName("mb_addr1")
    var mbAddr1: String? = null

    @SerializedName("mb_addr2")
    var mbAddr2: String? = null

    @SerializedName("mb_sex")
    var mbSex: String? = null

    @SerializedName("mb_sex_kor")
    var mbSexKor: String? = null

    @SerializedName("mb_age")
    var mbAge: String? = null

    @SerializedName("mb_birth")
    var mbBirth: String? = null

    @SerializedName("mb_char")
    var mbChar: String? = null

    @SerializedName("itemUse")
    var itemUse: String? = null


    @SerializedName("visit_num")
    var visitNum: String? = null

    @SerializedName("visit_log")
    var visitLog: String? = null


    var isChecked: Boolean = false

}
