package com.dmonster.darling.honey.agreement.presenter

import com.dmonster.darling.honey.agreement.data.UseData
import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface UseContract {

    interface View: BaseView {
        fun useListComplete(data: List<UseData>)    // 이용방법

        fun useListFailed(error: String?)    // 이용방법 호출 실패
    }

    interface Presenter: BasePresenter<View> {
        fun getUseList()    // 이용방법 호출
    }

}
