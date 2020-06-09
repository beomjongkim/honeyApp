package com.dmonster.darling.honey.profile.presenter

import android.content.Context
import android.net.Uri
import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.profile.data.ProfilePictureData

interface ImageDetailContract {

    interface View: BaseView {
        fun setTalkCheck(result: String?, roomNo: String?)    // 채팅방여부 확인

        fun setTalkComplete()    // 톡하기(채팅방으로 이동)

        fun setTalkFailed(error: String?)    // 톡하기 호출실패

        fun setItemUseComplete(type: String?, result: String?)    // 아이템 사용

        fun setItemUseFailed(error: String?)    // 아이템 사용 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getTalkCheck(id: String?, otherId: String?)    // 채팅방여부 확인

        fun getTalk(id: String?, otherId: String?, message: String?, talkImage: Uri?)    // 톡하기

        fun getItemCheck(id: String?, itemId: String?)    // 아이템 보유 확인

        fun setItemUse(context: Context, id: String?, itemId: String?, mbNo: String?, otherId: String?)    // 아이템 사용
    }

}
