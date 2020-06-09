package com.dmonster.darling.honey.profile.model

import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class GoodModel {

    /*    호감표현하기, 관심표현하기    */
    fun requestGood(id: String?, receiveId: String?, /*content: String?,*/ cardIndex: String?, subscriber: DisposableObserver<ResultItem<BaseItem>>) {
        RetrofitProtocol().retrofit.requestGood(ServerApi.instance.goodMethod, id, receiveId, /*content,*/ cardIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }
}
