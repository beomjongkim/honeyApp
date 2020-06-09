package com.dmonster.darling.honey.myinformation.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface IdealTypeContract {

    interface View: BaseView {
        fun setIdealTypeComplete(age: String?, area: String?, height: String?, weight: String?, style: String?,
                                 income: String?, education: String?, religion: String?, blood: String?)    // 이상형 정보

        fun setIdealTypeFailed(error: String?)    // 이상형 정보 호출실패

        fun setIdealTypeEditComplete()    // 이상형 수정

        fun setIdealTypeEditFailed(error: String?)    // 이상형 수정 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getIdealType(id: String?, mbNo: String?)    // 이상형 정보 불러오기

        fun setSelectIdealType(id: String?, age: String?, area: String?, height: String?, weight: String?,
                               style: String?, income: String?, education: String?, religion: String?, blood: String?)    // 이상형 수정
    }

}
