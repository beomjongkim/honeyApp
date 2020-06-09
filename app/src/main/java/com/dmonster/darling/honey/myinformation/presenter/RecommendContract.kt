package com.dmonster.darling.honey.myinformation.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.myinformation.data.RecommendData

interface RecommendContract {

    interface View: BaseView {
        fun setRecommendList(isScroll: Boolean, data: List<RecommendData>)    // 추천인 목록

        fun setRecommendListFailed(error: String?)    // 추천인 목록 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getRecommendList(isScroll: Boolean, id: String?, startCnt: String?, limitCnt: String?)    // 추천인 목록 불러오기
    }

}
