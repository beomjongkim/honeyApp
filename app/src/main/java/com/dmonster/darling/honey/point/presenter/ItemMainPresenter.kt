package com.dmonster.darling.honey.point.presenter

import com.dmonster.darling.honey.point.data.CoinData
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class ItemMainPresenter: ItemMainContract.Presenter {

    private lateinit var mModel: ItemModel
    private lateinit var mView: ItemMainContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: ItemMainContract.View) {
        this.mView = view
        this.mModel = ItemModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    현재 보유코인    */
    override fun getCoin(id: String?) {
        val subscriber = object: DisposableObserver<ResultItem<CoinData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setCoinFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<CoinData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setCoinComplete(it.item?.cntCoin, it.item?.evCoin)
                    }
                    else {
                        mView.setCoinFailed(it.message)
                    }
                }
            }
        }
        mModel.requestPossessionCoin(id, subscriber)
        subscription.add(subscriber)
    }

}
