package com.dmonster.darling.honey.profile.presenter

import android.content.Context
import android.net.Uri
import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.myinformation.data.PictureMarryData
import com.dmonster.darling.honey.profile.data.ProfilePictureData

interface ProfileContract {

    interface View: BaseView {
        fun setProfileSampleComplete(id: String?, gender: String?, talkId: String?, area: String?, genderStr: String?, type: String?, name: String?, age: String?,
                                     birth: String?, car: String?, myStyle: String?, introduce: String?, family: String?, data: ProfilePictureData?, marryData:PictureMarryData?,mbImgCnt : String?,  itemUse: String?)    // 프로필 열람전 정보

        fun setProfileSampleFailed(error: String?)    // 프로필 열람전 호출실패

        fun setProfileComplete(talkId: String?, area: String?, genderStr: String?, type: String?, name: String?, age: String?, birth: String?, income: String?, sibling: String?,
                               hometown: String?, job: String?, fortune: String?, education: String?, car: String?, religion: String?, parents: String?, marryPlan: String?,
                               divorce: String?, divorceYear: String?, children: String?, height: String?, weight: String?, drinking: String?, smoking: String?, blood: String?,
                               character: String?, hobby: String?, myStyle: String?, dateStyle: String?, introduce: String?, family: String?, data: ProfilePictureData?, marryData : PictureMarryData?,mbImgCnt : String?, route: String?)    // 프로필 열람후 모든 정보

        fun setProfileFailed(error: String?)    // 프로필 열람후 호출실패

        fun setTalkCheck(result: String?, roomNo: String?)    // 채팅방여부 확인

        fun setTalkComplete()    // 톡하기(채팅방으로 이동)

        fun setTalkFailed(error: String?)    // 톡하기 호출실패

        fun setItemUseComplete(type: String?, result: String?)    // 아이템 사용

        fun setItemUseFailed(error: String?)    // 아이템 사용 호출실패

        fun setBlockComplete(message: String?)    // 차단하기

        fun setBlockFailed(error: String?)    // 차단하기 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getProfileSample(context: Context, id: String?, mbNo: String?)    // 프로필 열람전 정보 불러오기

        fun getProfile(context: Context, id: String?, mbNo: String?)    // 프로필 열람후 모든정보 불러오기

        fun getTalkCheck(id: String?, otherId: String?)    // 채팅방여부 확인

        fun getTalk(id: String?, otherId: String?, message: String?, talkImage: Uri?)    // 톡하기

        fun getItemCheck(id: String?, itemId: String?)    // 아이템 보유 확인

        fun setItemUse(context: Context, id: String?, itemId: String?, mbNo: String?, otherId: String?)    // 아이템 사용

        fun setBlock(id: String?, mbNo: String?, type: String?)    // 차단하기
    }

}
