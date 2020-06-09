package com.dmonster.darling.honey.notice.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.notice.data.NoticeData

interface NoticeContract {

    interface View: BaseView {
        fun setNoticeList(data: List<NoticeData>)    // 공지사항 목록

        fun setNoticeListFailed(error: String?)    // 공지사항 목록 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getNoticeList(id: String?)    // 공지사항 목록 불러오기
    }

}
