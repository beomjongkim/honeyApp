package com.dmonster.darling.honey.agreement.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface AgreementContract {

    interface View: BaseView {
        fun agreementComplete(content: String?)    // 이용방법, 개인정보처리방침

        fun agreementFailed(error: String?)    // 이용방법, 개인정보처리방침 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getAgreement(types: String?)    // 이용방법, 개인정보처리방침 호출
    }

}
