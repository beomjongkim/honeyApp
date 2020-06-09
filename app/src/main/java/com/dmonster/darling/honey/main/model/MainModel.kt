package com.dmonster.darling.honey.main.model

import com.dmonster.darling.honey.main.data.NaviData
import com.dmonster.darling.honey.main.data.NoticePopupData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainModel {
    fun getNaviCount( id: String?, subscriber: DisposableObserver<ResultItem<NaviData>>) {
        RetrofitProtocol().retrofit.getNaviCount(ServerApi.instance.naviCountAlarmMethod, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }
    fun getNotice(subscriber: DisposableObserver<ResultItem<NoticePopupData>>) {
        RetrofitProtocol().retrofit.requestNotice(ServerApi.instance.mainNoticeMethod)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }
}