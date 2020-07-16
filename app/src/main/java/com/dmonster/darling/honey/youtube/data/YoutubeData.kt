package com.dmonster.darling.honey.youtube.data

import com.google.gson.annotations.SerializedName

/*    톡하기    */
class YoutubeData {

    @SerializedName("ya_no")
    var idx: String? = null

    @SerializedName("ya_link")
    var link: String? = null

    @SerializedName("ya_play_key")
    var playKey: String? = null

}
