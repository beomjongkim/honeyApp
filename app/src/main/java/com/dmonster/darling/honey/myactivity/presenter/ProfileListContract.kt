package com.dmonster.darling.honey.myactivity.presenter

import android.net.Uri
import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.myactivity.data.ProfileData

interface ProfileListContract {

    interface View: BaseView {
        fun setProfileListComplete(isScroll: Boolean, data: List<ProfileData>)    // 프로필 열람 목록

        fun setProfileListFailed(error: String?)    // 프로필 열람 목록 호출실패

        fun setProfileDeleteComplete(message: String?)    // 프로필 열람 회원 삭제

        fun setProfileDeleteFailed(error: String?)    // 프로필 열람 회원 삭제 호출실패

        fun setTalkCheck(result: String?, roomNo: String?)    // 채팅방여부 확인

        fun setItemCheckComplete(result: String?)    // 아이템 보유 확인

        fun setItemCheckFailed(error: String?)    // 아이템 보유 확인 호출실패

        fun setItemUseFailed(error: String?)    // 아이템 사용 호출실패

        fun setTalkComplete()    // 톡하기(채팅방으로 이동)

        fun setTalkFailed(error: String?)    // 톡하기 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getProfileList(isScroll: Boolean, startCnt: String?, limitCnt: String?, id: String?, type: String?)    // 프로필 열람 목록 불러오기

        fun setProfileDelete(id: String?, type: String?, idx: String?)    // 프로필 열람 회원 삭제

        fun getTalkCheck(id: String?, otherId: String?)    // 채팅방여부 확인

        fun getItemCheck(id: String?, itemId: String?)    // 아이템 보유 확인

        fun setItemUse(id: String?, itemId: String?, mbNo: String?, otherId: String?)    // 아이템 사용

        fun getTalk(id: String?, otherId: String?, message: String?, talkImage: Uri?)    // 톡하기
    }

}
