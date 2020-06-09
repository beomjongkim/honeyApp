package com.dmonster.darling.honey.myinformation.presenter

import com.dmonster.darling.honey.myinformation.data.RecommendData
import com.dmonster.darling.honey.myinformation.model.RecommendModel
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class RecommendPresenter: RecommendContract.Presenter {

    private lateinit var mModel: RecommendModel
    private lateinit var mView: RecommendContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: RecommendContract.View) {
        this.mView = view
        this.mModel = RecommendModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    추천인 목록    */
    override fun getRecommendList(isScroll: Boolean, id: String?, startCnt: String?, limitCnt: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<RecommendData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setRecommendListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<RecommendData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<RecommendData>()
                            mList.addAll(it)
                            mView.setRecommendList(isScroll, mList)
                        }
                    }
                    else {
                        mView.setRecommendListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestRecommend(id, startCnt, limitCnt, subscriber)
        subscription.add(subscriber)
    }

}
