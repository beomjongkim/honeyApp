package com.dmonster.darling.honey.question.presenter

import com.dmonster.darling.honey.base.BasePresenter
import com.dmonster.darling.honey.base.BaseView
import com.dmonster.darling.honey.question.data.QuestionData

interface QuestionContract {

    interface View: BaseView {
        fun setQuestionList(data: List<QuestionData>)    // 자주묻는 질문 목록

        fun setQuestionListFailed(error: String?)    // 자주묻는 질문 목록 호출실패
    }

    interface Presenter: BasePresenter<View> {
        fun getQuestionList()    // 자주묻는 질문 목록 불러오기
    }

}
