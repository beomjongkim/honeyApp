package com.dmonster.darling.honey.inquiry.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.inquiry.data.InquiryData

interface BreakdownContract {

    interface View: BaseView {
        fun setInquiryList(data: List<InquiryData>)    // 문의내역

        fun setInquiryListFailed(error: String?)    // 문의내역 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getInquiryList(id: String?)    // 문의내역 불러오기
    }

}
