package com.dmonster.darling.honey.agreement.data

import com.google.gson.annotations.SerializedName

/*    이용방법    */
class UseData {
    @SerializedName("co_id")
    var coId: String? = null

    @SerializedName("co_subject")
    var coSubject: String? = null

    @SerializedName("co_content")
    var coContent: String? = null

    @SerializedName("co_file")
    var coFile: String? = null

}
