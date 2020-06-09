package com.dmonster.darling.honey.inquiry.presenter

import android.net.Uri
import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView

interface InquireContract {

    interface View: BaseView {
        fun setInquireComplete()    // 문의하기

        fun setInquireFailed(error: String?)    // 문의하기 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getInquire(id: String?, type: String?, image: Uri?, phone: String?, content: String?)    // 문의하기
    }

}
