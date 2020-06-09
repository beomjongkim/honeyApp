package com.dmonster.darling.honey.myactivity.presenter

import android.net.Uri
import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.myactivity.data.MemberData

interface LoveListContract {

    interface View: BaseView {
        fun setLoveListComplete(isScroll: Boolean, data: List<MemberData>)    // 러브카드 목록

        fun setLoveListFailed(error: String?)    // 러브카드 목록 호출실패

        fun setTalkCheck(result: String?, roomNo: String?)    // 채팅방여부 확인

        fun setItemCheckComplete(result: String?)    // 아이템 보유 확인

        fun setItemCheckFailed(error: String?)    // 아이템 보유 확인 호출실패

        fun setItemUseFailed(error: String?)    // 아이템 사용 호출실패

        fun setTalkComplete()    // 톡하기(채팅방으로 이동)

        fun setTalkFailed(error: String?)    // 톡하기 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getLoveList(isScroll: Boolean, startCnt: String?, limitCnt: String?, id: String?)    // 러브카드 목록 불러오기

        fun getTalkCheck(id: String?, otherId: String?)    // 채팅방여부 확인

        fun getItemCheck(id: String?, itemId: String?)    // 아이템 보유 확인

        fun setItemUse(id: String?, itemId: String?, mbNo: String?, otherId: String?)    // 아이템 사용

        fun getTalk(id: String?, otherId: String?, message: String?, talkImage: Uri?)    // 톡하기
    }

}
