package com.dmonster.darling.honey.servicecenter.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface ServiceCenterContract {

    interface View: BaseView {
        fun setAppInfo(email: String?, phone: String?, communication: String?, business: String?)    // 앱정보

        fun setAppInfoFailed(error: String?)    // 앱정보 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getAppInfo()    // 앱정보 불러오기
    }

}
