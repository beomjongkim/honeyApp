package com.dmonster.darling.honey.information.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.information.data.MyInfoData

interface MyInfoContract {

    interface View: BaseView {
        fun setMyInfoComplete(email: String?, talkId: String?, age: String?, type: String?, area01: String?, area02: String?,
                              phone: String?, talkIdState: String?, ageState: String?, typeState: String?)    // 기본정보 불러오기

        fun setMyInfoFailed(error: String?)  // 기본정보 호출실패

        fun setMyInfoEditComplete(talkId: String?, age: String?, birth: String?, type: String?, area01: String?, area02: String?, phone: String?)    // 기본정보 수정

        fun setMyInfoEditFailed(error: String?)    // 기본정보 수정 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getMyInfo(id: String?, mbNo: String?)    // 기본정보 불러오기

        fun setMyInfoEdit(id: String?, talkId: String?, age: String?, type: String?,area01: String?, area02: String?,
                          password: String?, newPassword: String?, newPasswordCheck: String?, phone: String?)    // 기본정보 수정
    }

}
