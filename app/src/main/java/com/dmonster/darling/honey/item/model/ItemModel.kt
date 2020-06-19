package com.dmonster.darling.honey.item.model

import com.dmonster.darling.honey.item.data.*
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ItemModel {

    /*    현재보유코인    */
    fun requestPossessionCoin(id: String?, subscriber: DisposableObserver<ResultItem<CoinData>>) {
        RetrofitProtocol().retrofit.requestPossessionCoin(ServerApi.instance.possessionCoinMethod, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    내 아이템 조회    */
    fun requestItemList(id: String?, subscriber: DisposableObserver<ResultListItem<ItemData>>) {
        RetrofitProtocol().retrofit.requestItemList(ServerApi.instance.itemListMethod, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    아이템 사용내역    */
    fun requestItemUseList(id: String?, subscriber: DisposableObserver<ResultListItem<ItemUseData>>) {
        RetrofitProtocol().retrofit.requestItemUseList(ServerApi.instance.itemUseMethod, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    마켓 상품목록    */
    fun requestMarketList(id: String?, subscriber: DisposableObserver<ResultListItem<MarketListData>>) {
        RetrofitProtocol().retrofit.requestMarket(ServerApi.instance.marketListMethod, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    마켓 상품구매, 선물    */
    fun requestMarketBuy(id: String?, productId: String?, count: String?, type: String?, otherId: String?, subscriber: DisposableObserver<ResultItem<MarketBuyData>>) {
        RetrofitProtocol().retrofit.requestMarketBuy(ServerApi.instance.marketBuyMethod, id, productId, count, type, otherId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    코인충전    */
    fun requestCoinCharge(id: String?, case: String?, coin: String?, orderId: String?, productId: String?, subscriber: DisposableObserver<ResultItem<BaseItem>>) {
        RetrofitProtocol().retrofit.requestCoinCharge(ServerApi.instance.coinChargeMethod, id, case, coin, orderId, productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    /*    이용권 여부   */
    fun check_own_freepass(id: String?, subscriber: DisposableObserver<ResultItem<CheckFreePassData>>) {
        RetrofitProtocol().retrofit.checkOwnFreepass(ServerApi.instance.checkOwnFreepass, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    /*    이용권 구매 기록가져오기   */
    fun get_log_point(id: String?, subscriber: DisposableObserver<ResultListItem<PointLogData>>) {
        RetrofitProtocol().retrofit.readPointLog(ServerApi.instance.readPointLog, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    /*    이용권 구매 기록가져오기   */
    fun read_point(id: String?, subscriber: DisposableObserver<ResultItem<BaseItem>>) {
        RetrofitProtocol().retrofit.readPoint(ServerApi.instance.readPoint, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    /*    이용권 구매하기  */
    fun buyItem(id: String?, it_id : Int?, subscriber: DisposableObserver<ResultItem<BaseItem>>) {
        RetrofitProtocol().retrofit.buyItem(ServerApi.instance.buyItem, id, it_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }
}
