package com.dmonster.darling.honey.login.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface LoginContract {

    interface View: BaseView {
        fun setLoginComplete(loginId: String?, mbNo: String?, recommend: String?, gender: String?,mbNick : String?, dormantState: String?, profileState: String?)    // 로그인

        fun setLoginFailed(error: String?)    // 로그인 호출실패

        fun setSocialLoginComplete(loginId: String?, mbNo: String?, recommend: String?, gender: String?, mbNick : String?, dormantState: String?, profileState: String?)    // 소셜 로그인

        fun setSocialLoginFailed()    // 소셜 로그인 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun setLogin(id: String?, password: String?, instanceId: String?, type: String?)    // 로그인

        fun setSocialLogin(id: String?, instanceId: String?, type: String?)    // 소셜 로그인
    }

}
