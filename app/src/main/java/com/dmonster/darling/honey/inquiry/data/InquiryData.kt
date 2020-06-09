package com.dmonster.darling.honey.inquiry.data

import com.google.gson.annotations.SerializedName

/*    문의내역    */
class InquiryData {

    @SerializedName("wr_id")
    var wrId: String? = null

    @SerializedName("ca_name")
    var caName: String? = null

    @SerializedName("wr_subject")
    var wrSubject: String? = null

    @SerializedName("wr_content")
    var wrContent: String? = null

    @SerializedName("wr_datetime")
    var wrDatetime: String? = null

    @SerializedName("wr_img")
    var wrImg: String? = null

    @SerializedName("reply_id")
    var replyId: String? = null

    @SerializedName("reply_name")
    var replyName: String? = null

    @SerializedName("reply_content")
    var replyContent: String? = null

    @SerializedName("reply_datetime")
    var replyDatetime: String? = null

}
