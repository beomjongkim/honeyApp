package com.dmonster.darling.honey.main.presenter

import android.content.Context
import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.main.data.MainListData
import com.dmonster.darling.honey.notice.data.NoticeData

interface MainListContract {

    interface View: BaseView {
        fun setMainList(isScroll: Boolean, data: List<MainListData>)    // 회원목록

        fun setMainListFailed(isScroll: Boolean, error: String?)    // 회원목록 호출실패

        fun getRefresh(result: String?)    // 자동갱신 여부 확인

        fun setRefreshListComplete()    // 위로 올리기

        fun setRefreshListFailed(error: String?)    // 목록 새로고침 호출실패

        fun setNoticeList(data: List<NoticeData>)    // 공지사항 목록

        fun setNoticeListFailed(error: String?)    // 공지사항 목록 호출실패

        fun setTalkCheck(result: String?, roomNo: String?)    // 채팅방 여부 확인

        fun setItemUseComplete(type: String?, result: String?)    // 아이템 사용

        fun setItemUseFailed(error: String?)    // 아이템 사용 호출실패

        fun setTalkComplete()    // 톡하기(채팅방으로 이동)

        fun setTalkFailed(error: String?)    // 톡하기 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getMainList(isScroll: Boolean, startCnt: String?, limitCnt: String?, id: String?, area: String?, gender: String?, age: String?)    // 회원목록 불러오기

        fun setRefresh(id: String?, itemId: String?)    // 자동갱신 여부 확인

        fun setRefreshList(id: String?)    // 위로 올리기

        fun getNoticeList(id: String?)    // 공지사항 목록 불러오기

        fun getTalkCheck(id: String?, otherId: String?)    // 채팅방 여부 확인

        fun getItemCheck(id: String?, itemId: String?)    // 아이템 보유 확인

        fun setItemUse(context: Context, id: String?, itemId: String?, mbNo: String?, otherId: String?)    // 아이템 사용
    }

}
