package com.dmonster.darling.honey.agreement.data

import com.google.gson.annotations.SerializedName

/*    이용약관, 개인정보처리방침    */
class AgreementData {

    @SerializedName("co_id")
    var coId: String? = null

    @SerializedName("co_subject")
    var coSubject: String? = null

    @SerializedName("co_content")
    var coContent: String? = null

}
