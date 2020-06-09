package com.dmonster.darling.honey.question.data

import com.google.gson.annotations.SerializedName

/*    자주묻는 질문    */
class QuestionData {

    @SerializedName("fa_id")
    var faId: String? = null

    @SerializedName("fm_id")
    var fmId: String? = null

    @SerializedName("fa_subject")
    var faSubject: String? = null

    @SerializedName("fa_content")
    var faContent: String? = null

}
