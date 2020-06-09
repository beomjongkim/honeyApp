package com.dmonster.darling.honey.option.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 class SimpleMyInfoData (

    @SerializedName("mb_nick")
     var mbNick: String? = null,

    @SerializedName("coin")
     var coin: String? = null,

    @SerializedName("mb_img")
     var mbImg: PictureData? = null
){
     /*    내정보 프로필 사진    */
     data class PictureData(
         @SerializedName("mb_img")
         var mbImg: ArrayList<String>? = null,

         @SerializedName("mb_img_thumb")
         var mbImgThumb: ArrayList<String>? = null,

         @SerializedName("mb_img_no")
         var mbImgNo: ArrayList<String>? = null
     )

 }