package com.dmonster.darling.honey.profile.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface GoodContract {

    interface View: BaseView {
        fun setGoodComplete(result: String?)    // 호감있어요, 관심있어요

        fun setGoodFailed(error: String?)    // 호감있어요, 관심있어요 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun setGood(id: String?, receiveId: String?, /*content: String?,*/ cardIndex: String?)    // 호감있어요, 관심있어요
    }

}
