package com.dmonster.darling.honey.alarm.data

import com.google.gson.annotations.SerializedName

/*    알림설정    */
class AlarmData {

    @SerializedName("mb_push_yn")
    var mbPushYn: String? = null

    @SerializedName("mb_push_msg")
    var mbPushMsg: String? = null

    @SerializedName("mb_push_hogam")
    var mbPushHogam: String? = null

    @SerializedName("mb_push_mb")
    var mbPushMb: String? = null

    @SerializedName("mb_push_loveCard")
    var mbPushLoveCard: String? = null

    @SerializedName("mb_push_notice")
    var mbPushNotice: String? = null

}
