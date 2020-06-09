package com.dmonster.darling.honey.myinformation.data

import com.google.gson.annotations.SerializedName

/*    내정보 프로필 사진    */
data class PictureData(
    @SerializedName("mb_img")
    var mbImg: ArrayList<String>? = null,

    @SerializedName("mb_img_thumb")
    var mbImgThumb: ArrayList<String>? = null,

    @SerializedName("mb_img_no")
    var mbImgNo: ArrayList<String>? = null
)
