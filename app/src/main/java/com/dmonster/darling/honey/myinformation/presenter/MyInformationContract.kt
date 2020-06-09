package com.dmonster.darling.honey.myinformation.presenter

import android.content.Context
import android.net.Uri
import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.myinformation.data.PictureData
import com.dmonster.darling.honey.myinformation.data.PictureMarryData

interface MyInformationContract {

    interface View: BaseView {
        fun setMyInformationComplete(id: String?, profileState: String?, talkId: String?, area: String?, type: String?, firstName: String?, lastName: String?, nameCheck: String?, age: String?, birth: String?,
                                     siblingMale: String?, siblingFemale: String?, siblingNumber: String?, children: String?, hometown01: String?, hometown02: String?, job: String?, income: String?,
                                     fortune: String?, education: String?, car: String?, religion: String?, parents: String?, marryPlan: String?, divorce: String?, divorceYear: String?,
                                     height: String?, weight: String?, drinking: String?, drinkingNumber: String?, smoking: String?, blood: String?, character: String?, hobby: String?,
                                     myStyle: String?, dateStyle: String?, introduce: String?, textColor: String?, family: String?, route: String?, phone: String?, data: PictureData?, marryData : PictureMarryData?)    // 내정보

        fun setMyInformationFailed(error: String?)    // 내정보 호출실패

        fun setMyInformationEditComplete(profileState: String?, first: Boolean = false)    // 내정보 수정

        fun setMyInformationEditFailed(error: String?)    // 내정보 수정 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getMyInformation(context: Context, id: String?, mbNo: String?)    // 내정보 불러오기

        fun setMyInformationEdit(id: String?, area01: String?, area02: String?, type: String?, firstName: String?, lastName: String?, nameCheck: String?, age: String?, siblingMale: String?,
                                 siblingFemale: String?, siblingNumber: String?, children: String?, hometown01: String?, hometown02: String?, job: String?, income: String?, fortune: String?,
                                 education: String?, car: String?, religion: String?, parents: String?, marryPlan: String?, divorce: String?, divorceYear: String?, height: String?, weight: String?,
                                 drinking: String?, drinkingNumber: String?, smoking: String?, blood: String?, character: String?, hobby: String?, myStyle: String?, dateStyle: String?,
                                 introduce: String?, textColor: String?, family: String?, route: String?, phone: String?, deleteImage: String?, deleteMarryImage : String?,  profileImage: ArrayList<Uri>?,marryCertImage:ArrayList<Uri>?)    // 내정보 수정
    }

}
