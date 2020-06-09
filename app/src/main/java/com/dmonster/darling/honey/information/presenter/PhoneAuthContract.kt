package com.dmonster.darling.honey.information.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.information.data.MyInfoData

interface PhoneAuthContract {

    interface View: BaseView {
        fun setPhoneAuthComplete(message: String?)    // 인증번호 받기

        fun setPhoneAuthFailed(error: String?)    // 인증번호 호출실패

        fun setPhoneAuthCheckComplete()    // 휴대폰 인증

        fun setPhoneAuthCheckFailed(error: String?)    // 휴대폰 인증 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getPhoneAuth(id: String?, phone: String?)    // 인증번호 받기

        fun getPhoneAuthCheck(phone: String?, authNo: String?)    // 휴대폰 인증
    }

}
