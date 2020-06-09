package com.dmonster.darling.honey.join.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface JoinContract {

    interface View: BaseView {
        fun setJoinIdIsValid(result: String?)    // 회원가입 아이디 사용여부

        fun setJoinComplete(joinId: String?, mbNo: String?, recommend: String?, gender: String?)    // 회원가입

        fun setJoinFailed(error: String?)    // 회원가입 호출실패

        fun setLoginComplete()    // 로그인

        fun setLoginFailed()    // 로그인 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getJoinId(id: String?)    // 회원가입 아이디 사용여부

        fun getJoin(id: String?, email: String?, password: String?, passwordCheck: String?, talkId: String?, age: String?,
                    area01: String?, area02: String?, gender: String?, recommendation: String?, phone: String?)    // 회원가입

        fun setLogin(id: String?, password: String?, instanceId: String?, type: String?)    // 로그인
    }

}
