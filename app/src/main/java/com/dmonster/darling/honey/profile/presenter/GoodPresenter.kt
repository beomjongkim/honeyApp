package com.dmonster.darling.honey.profile.presenter

import com.dmonster.darling.honey.profile.model.GoodModel
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class GoodPresenter: GoodContract.Presenter {

    private lateinit var mModel: GoodModel
    private lateinit var mView: GoodContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: GoodContract.View) {
        this.mView = view
        this.mModel = GoodModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    호감있어요, 관심있어요    */
    override fun setGood(id: String?, receiveId: String?, /*content: String?,*/ cardIndex: String?) {
        val subscriber = object: DisposableObserver<ResultItem<BaseItem>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setGoodFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<BaseItem>) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setGoodComplete(it.message)
                    }
                    else {
                        mView.setGoodFailed(it.message)
                    }
                }
            }
        }
        mModel.requestGood(id, receiveId, /*content,*/ cardIndex, subscriber)
        subscription.add(subscriber)
    }

}
