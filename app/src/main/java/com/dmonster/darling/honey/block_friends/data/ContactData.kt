package com.dmonster.darling.honey.block_friends.data

import com.google.gson.annotations.SerializedName

class ContactData {

    @SerializedName("mb_id")
    var mbId: String? = null


    @SerializedName("mb_nick")
    var mbNick: String? = null

    @SerializedName("mb_hp")
    var mbHp: String? = null
}