package com.dmonster.darling.honey.myactivity.data

import com.google.gson.annotations.SerializedName

/*    톡하기    */
class TalkListData {

    @SerializedName("idx")
    var idx: String? = null

    @SerializedName("chat_host_id")
    var chatHostId: String? = null

    @SerializedName("chat_guest_id")
    var chatGuestId: String? = null

    @SerializedName("reg_date")
    var regDate: String? = null

    @SerializedName("last_date")
    var lastDate: String? = null

    @SerializedName("chat_msg")
    var chatMsg: String? = null

    @SerializedName("chat_sender")
    var chatSender: String? = null

    @SerializedName("chat_send_date")
    var chatSendDate: String? = null

    @SerializedName("chat_type")
    var chatType: String? = null

    @SerializedName("mb_img")
    var mbImg: String? = null

    @SerializedName("mb_img_thumb")
    var mbImgThumb: String? = null

    @SerializedName("mb_no")
    var mbNo: String? = null

    @SerializedName("mb_id")
    var mbId: String? = null

    @SerializedName("mb_name")
    var mbName: String? = null

    @SerializedName("mb_nick")
    var mbNick: String? = null

    @SerializedName("mb_sex")
    var mbSex: String? = null

    @SerializedName("mb_age")
    var mbAge: String? = null

    @SerializedName("mb_animal")
    var mbAnimal: String? = null

    @SerializedName("mb_char")
    var mbChar: String? = null

    @SerializedName("mb_addr1")
    var mbAddr1: String? = null

    @SerializedName("mb_wish_mark")
    var mbWishMark: String? = null

    @SerializedName("not_read")
    var notRead: String? = null

    var isChecked: Boolean = false
}
