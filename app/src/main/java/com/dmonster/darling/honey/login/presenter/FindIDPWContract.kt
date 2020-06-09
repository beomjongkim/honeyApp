package com.dmonster.darling.honey.login.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface FindIDPWContract {

    interface View: BaseView {
        fun setFindIDComplete(id: String?)    // 아이디 찾기

        fun setFindIDFailed(error: String?)    // 아이디 찾기 호출실패

        fun setFindPWComplete(message: String?)    // 비밀번호 찾기

        fun setFindPWFailed(error: String?)    // 비밀번호 찾기 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun setFindID(type: String?, phone: String?, id: String?)    // 아이디 찾기

        fun setFindPW(type: String?, phone: String?, id: String?)    // 비밀번호 찾기
    }

}
