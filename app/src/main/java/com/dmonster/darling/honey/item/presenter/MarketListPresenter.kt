package com.dmonster.darling.honey.item.presenter

import com.dmonster.darling.honey.item.data.CoinData
import com.dmonster.darling.honey.item.data.MarketBuyData
import com.dmonster.darling.honey.item.data.MarketListData
import com.dmonster.darling.honey.item.model.ItemModel
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class MarketListPresenter: MarketListContract.Presenter {

    private lateinit var mModel: ItemModel
    private lateinit var mView: MarketListContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: MarketListContract.View) {
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
                        mView.setCoinComplete(it.item?.cntCoin)
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

    /*    마켓 상품목록    */
    override fun getMarketList(isScroll: Boolean, id: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<MarketListData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setMarketListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<MarketListData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<MarketListData>()
                            mList.addAll(it)
                            mView.setMarketListComplete(isScroll, mList)
                        }
                    }
                    else {
                        mView.setMarketListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestMarketList(id, subscriber)
        subscription.add(subscriber)
    }

    /*    마켓 상품구매    */
    override fun setMarketItemBuy(id: String?, productId: String?, count: String?, type: String?, otherId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<MarketBuyData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setMarketItemBuyFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<MarketBuyData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setMarketItemBuyComplete()
                    }
                    else {
                        mView.setMarketItemBuyFailed(it.message)
                    }
                }
            }
        }
        mModel.requestMarketBuy(id, productId, count, type, otherId, subscriber)
        subscription.add(subscriber)
    }

}
