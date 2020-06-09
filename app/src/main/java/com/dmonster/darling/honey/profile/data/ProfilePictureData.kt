package com.dmonster.darling.honey.profile.data

import com.google.gson.annotations.SerializedName

/*    프로필 사진    */
class ProfilePictureData {

    @SerializedName("mb_img")
    var mbImg: ArrayList<String>? = null

    @SerializedName("mb_img_thumb")
    var mbImgThumb: ArrayList<String>? = null

}
