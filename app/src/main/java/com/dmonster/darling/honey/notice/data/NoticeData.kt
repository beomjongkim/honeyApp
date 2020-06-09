package com.dmonster.darling.honey.notice.data

import com.google.gson.annotations.SerializedName

/*    공지사항    */
class NoticeData {

    @SerializedName("wr_id")
    var wrId: String? = null

    @SerializedName("wr_subject")
    var wrSubject: String? = null

    @SerializedName("wr_content")
    var wrContent: String? = null

    @SerializedName("wr_datetime")
    var wrDatetime: String? = null

    @SerializedName("wr_img")
    var wrImg: String? = null

}
