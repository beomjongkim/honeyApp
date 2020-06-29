package com.dmonster.darling.honey.magazine.data

import com.google.gson.annotations.SerializedName

class MagazineData {

    @SerializedName("mg_type")
    var type: String? = null

    @SerializedName("mg_thumnail")
    var thumnail: String? = null

    @SerializedName("mg_title")
    var title: String? = null

    @SerializedName("mg_link")
    var link: String? = null

    @SerializedName("mg_on_updated")
    var onUpdated: String? = null

}