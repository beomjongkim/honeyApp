package com.dmonster.darling.honey.intro.presenter

import android.widget.ImageView
import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface IntroLoginContract {

    interface View: BaseView {

        fun setLoginComplete(mbNo: String?, mbNick : String?, recommend: String?, gender: String?, dormantState: String?, profileState: String?)    // 로그인

        fun setLoginFailed(error: String?)    // 로그인 호출실패
    }

    interface Presenter: BasePresenter<View> {


        fun setLogin(id: String?, password: String?, instanceId: String?, type: String?)    // 로그인

        fun setRandomImageView(view : ImageView, intArray : IntArray) //이미지 뷰 랜덤으로 세팅하기..
    }

}
