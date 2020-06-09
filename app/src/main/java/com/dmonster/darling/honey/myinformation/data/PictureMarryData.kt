package com.dmonster.darling.honey.myinformation.data

import com.google.gson.annotations.SerializedName

class PictureMarryData {

    @SerializedName("mb_marry_img")
    var mbImg: ArrayList<String>? = null

    @SerializedName("mb_marry_img_thumb")
    var mbImgThumb: ArrayList<String>? = null

    @SerializedName("mb_marry_img_no")
    var mbImgNo: ArrayList<String>? = null

}