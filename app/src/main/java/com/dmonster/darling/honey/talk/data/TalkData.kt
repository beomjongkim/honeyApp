package com.dmonster.darling.honey.talk.data

import com.google.gson.annotations.SerializedName

/*    톡하기    */
class TalkData {

    @SerializedName("idx")
    var idx: String? = null

    @SerializedName("chat_room_no")
    var chatRoomNo: String? = null

    @SerializedName("chat_sender")
    var chatSender: String? = null

    @SerializedName("chat_receiver")
    var chatReceiver: String? = null

    @SerializedName("chat_msg")
    var chatMsg: String? = null

    @SerializedName("chat_img")
    var chatImg: String? = null

    @SerializedName("chat_type")
    var chatType: String? = null

    @SerializedName("chat_send_date")
    var chatSendDate: String? = null

    @SerializedName("chat_read_date")
    var chatReadDate: String? = null

    @SerializedName("mb_img")
    var mbImg: String? = null

    @SerializedName("mb_img_thumb")
    var mbImgThumb: String? = null

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

}
