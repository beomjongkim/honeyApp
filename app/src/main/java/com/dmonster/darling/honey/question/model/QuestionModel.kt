package com.dmonster.darling.honey.question.model

import com.dmonster.darling.honey.question.data.QuestionData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class QuestionModel {

    /*    자주묻는 질문 목록    */
    fun requestQuestionList(subscriber: DisposableObserver<ResultListItem<QuestionData>>) {
        RetrofitProtocol().retrofit.requestQuestion(ServerApi.instance.questionMethod, "0", "50")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }
}
