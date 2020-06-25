package com.dmonster.darling.honey.point.model

import com.dmonster.darling.honey.point.data.PointData
import com.dmonster.darling.honey.point.data.PointLogData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class PointModel {
    /*    포인트 기록가져오기   */
    fun get_log_point(id: String?, subscriber: DisposableObserver<ResultListItem<PointLogData>>) {
        RetrofitProtocol().retrofit.readPointLog(ServerApi.instance.readPointLog, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    /*   현재 포인트 가져오기   */
    fun read_point(id: String?, subscriber: DisposableObserver<ResultItem<PointData>>) {
        RetrofitProtocol().retrofit.readPoint(ServerApi.instance.readPoint, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    fun reserve_payment(id: String?, name :String?, price : Int?, subscriber: DisposableObserver<ResultItem<String>>){
        RetrofitProtocol().retrofit.reservePayment(ServerApi.instance.reservePayment, id, name, price)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

}