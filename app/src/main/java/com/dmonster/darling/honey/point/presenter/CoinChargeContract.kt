package com.dmonster.darling.honey.point.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface CoinChargeContract {

    interface View: BaseView {
        fun setCoinChargeComplete(message: String?)    // 코인충전

        fun setCoinChargeFailed(error: String?)    // 코인충전 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun setCoinCharge(id: String?, case: String?, coin: String?, orderId: String?, productId: String?)    // 코인충전
    }

}
