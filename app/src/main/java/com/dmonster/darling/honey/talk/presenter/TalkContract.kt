package com.dmonster.darling.honey.talk.presenter

import android.net.Uri
import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.talk.data.TalkData

interface TalkContract {

    interface View: BaseView {
        fun setTalkListComplete(isScroll: Boolean, data: ArrayList<TalkData>)    // 채팅내역

        fun setTalkListFailed(error: String?)    // 채팅내역 호출실패

        fun setItemCheckComplete(result: String?)    // 아이템 보유 확인

        fun setItemCheckFailed(error: String?)    // 아이템 확인 호출실패

        fun setSendTalkComplete(time: String?, message: String?, talkImage: String?)    // 채팅전송

        fun setSendTalkFailed(error: String?)    // 채팅전송 실패

        fun setBlockComplete(message: String?)    // 회원 차단하기

        fun setBlockFailed(error: String?)    // 회원 차단 호출실패

        fun setDeleteComplete()    // 채팅방 삭제

        fun setDeleteFailed(error: String?)    // 채팅방 삭제 호출실패

        fun setBookmark(message: String?)    // 즐겨찾기
    }

    interface Presenter: BasePresenter<View> {
        fun getTalkList(isScroll: Boolean, startCnt: String?, limitCnt: String?, id: String?, roomNo: String?)    // 채팅내역 불러오기

        fun getItemCheck(id: String?, itemId: String?)    // 아이템 보유 확인

        fun setSendTalk(id: String?, otherId: String?, message: String?, talkImage: Uri?)    // 채팅 메세지 전송

        fun setBlock(id: String?, mbNo: String?, type: String?)    // 회원 차단하기

        fun setDelete(id: String?, roomNo: String?)    // 채팅방 삭제

        fun setBookmark(id: String?, mbNo: String?, type: String?)    // 즐겨찾기
    }

}
