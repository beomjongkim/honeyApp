package com.dmonster.darling.honey.item.presenter

import com.dmonster.darling.honey.item.model.ItemModel
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class CoinChargePresenter: CoinChargeContract.Presenter {

    private lateinit var mModel: ItemModel
    private lateinit var mView: CoinChargeContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: CoinChargeContract.View) {
        this.mView = view
        this.mModel = ItemModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    코인충전    */
    override fun setCoinCharge(id: String?, case: String?, coin: String?, orderId: String?, productId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<BaseItem>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setCoinChargeFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<BaseItem>) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setCoinChargeComplete(it.message)
                    }
                    else {
                        mView.setCoinChargeFailed(it.message)
                    }
                }
            }
        }
        mModel.requestCoinCharge(id, case, coin, orderId, productId, subscriber)
        subscription.add(subscriber)
    }

}
