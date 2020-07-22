package com.dmonster.darling.honey.myactivity.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.myactivity.data.TalkListData

interface TalkListContract {

    interface View: BaseView {
        fun setTalkList(isScroll: Boolean, data: List<TalkListData>)    // 톡하기 목록

        fun setTalkListFailed(error: String?)    // 톡하기 목록 호출실패

        fun setTalkDeleteComplete(message: String?)    // 톡하기 삭제

        fun setTalkDeleteFailed(error: String?)    // 톡하기 삭제 호출실패

        fun setItemCheckComplete(result: String?)    // 아이템 보유 확인

        fun setItemCheckFailed(error: String?)    // 아이템 보유 확인 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getTalkList(isScroll: Boolean, id: String?, startCnt : String?, limitCnt: String?)    // 톡하기 목록 불러오기

        fun setTalkDelete(id: String?, idx: String?)    // 톡하기 삭제

        fun getItemCheck(id: String?, itemId: String?)    // 아이템 보유 확인
    }

}
