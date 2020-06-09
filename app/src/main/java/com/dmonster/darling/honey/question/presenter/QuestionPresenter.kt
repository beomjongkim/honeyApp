package com.dmonster.darling.honey.question.presenter

import com.dmonster.darling.honey.question.data.QuestionData
import com.dmonster.darling.honey.question.model.QuestionModel
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class QuestionPresenter: QuestionContract.Presenter {

    private lateinit var mModel: QuestionModel
    private lateinit var mView: QuestionContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: QuestionContract.View) {
        this.mView = view
        this.mModel = QuestionModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    자주묻는 질문 목록    */
    override fun getQuestionList() {
        val subscriber = object: DisposableObserver<ResultListItem<QuestionData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setQuestionListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<QuestionData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<QuestionData>()
                            mList.addAll(it)
                            mView.setQuestionList(mList)
                        }
                    }
                    else {
                        mView.setQuestionListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestQuestionList(subscriber)
        subscription.add(subscriber)
    }

}
