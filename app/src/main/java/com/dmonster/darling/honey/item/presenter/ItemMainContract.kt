package com.dmonster.darling.honey.item.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface ItemMainContract {

    interface View: BaseView {
        fun setCoinComplete(nowCoin: String?, saveCoin: String?)    // 현재 보유코인

        fun setCoinFailed(error: String?)    // 현재 보유코인 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getCoin(id: String?)    // 현재 보유코인
    }

}
